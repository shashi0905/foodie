# Foodie Implementation Plan

This plan turns the BUSINESS_CONTEXT and TECHNICAL_CONTEXT into a concrete, phased backlog.

---

## Phase 1 – Secure Single‑Restaurant MVP (End‑to‑End)

Focus: one restaurant per order, full flow from auth → browse/search → cart → order, with secure APIs and future‑proof models.

### 1. Gateway Auth & Routing
- Add JWT validation in api-gateway for `/api/orders/**`, `/api/restaurants/**`, `/api/cart/**`, reusing user-service’s JWT structure.
- Forward `Authorization` and correlation headers (e.g., `X-Correlation-Id`) to downstream services.

### 2. Controller-Level RBAC (v1)
- Add `@PreAuthorize` on:
  - `OrderController.createOrder` – require `ROLE_CUSTOMER` and authenticated user == `orderRequestDTO.customerId`.
  - `OrderController.getOrder` and future list endpoint – only owning customer or `ROLE_ADMIN`.
  - `CartController.addItemToCart`, `removeItemFromCart`, `getCart` – only authenticated customer matching `customerId` (and optionally admin).
  - `RestaurantController.addRestaurant`, `delete` – `ROLE_RESTAURANT_OWNER` (owning user) or `ROLE_ADMIN`.
  - `UserController.getCurrentUserProfile` – any authenticated user.
  - `UserController.getUserById` – same user or `ROLE_ADMIN`.
- Introduce a small security helper bean (e.g., in user-service/security) exposing methods like `isAdmin()` and `isCurrentUser(userId)` for SpEL expressions in `@PreAuthorize`.

### 3. Single‑Restaurant Order Enforcement (Phase 1 Rule)
- In `OrderServiceImpl.createOrder`, validate that all `OrderItemDTO`s in `OrderRequestDTO` share the same `restaurantId`; reject mixed‑restaurant orders.
- Keep per-item `restaurantId` in `OrderItemDTO` (no top‑level `restaurantId`), so the DTO shape can later support multi‑restaurant orders by relaxing this rule.

### 4. Order Validation Against Restaurant-Service
- Use `RestaurantServiceClient` inside `OrderServiceImpl.createOrder` to:
  - Fetch the authoritative menu for the given `restaurantId` from restaurant-service.
  - Validate each requested item’s ID/name/price against that menu before computing totals.
- Fail order creation clearly if validation or restaurant-service calls fail.

### 5. Order Lifecycle Basics & Reads
- Add a `status` field to `Order` using the existing `Status` enum; set `Status.NEW` on creation and return it in `OrderResponseDTO`.
- Implement:
  - `getOrder(id)` with an ownership check (customer/admin via `@PreAuthorize`).
  - `getOrderByCustomer(customerId)` as backing for a "my orders" view, scoped to the authenticated customer.

### 6. Cart Model & Cart→Order Contract (Future‑Ready)
- Keep `Cart` as `(id, customerId, List<CartItem>)` for Phase 1.
- Define and document a mapping from cart to `OrderRequestDTO`:
  - Phase 1: enforce a single `restaurantId` per order via validation.
  - Future: allow multiple `restaurantId`s by grouping items per restaurant.
- Prepare to add `restaurantId` to `CartItem` later without changing CartController or CartService signatures.

### 7. Persistence & Minimal Observability
- Move order-service to PostgreSQL for non‑dev profiles; keep H2 only for local tests.
- Generate/propagate a correlation ID header (e.g., `X-Correlation-Id`) at api-gateway and log it in all services, together with `userId` / `orderId` where applicable, using a simple ELK/OpenTelemetry‑friendly log format.

---

## Phase 2 – Approvals, Ownership & Business Rules

Focus: enforce business policies (approvals, ownership, cancellation, status transitions) and harden behavior.

### 1. Admin Role Management & Approvals
- In user-service, add admin-only APIs (using `@PreAuthorize("hasRole('ADMIN')")`) to:
  - List users and assign/revoke roles (customer, restaurant owner, delivery driver, admin).
  - Manage an approval status (e.g., PENDING/APPROVED/REJECTED) for restaurant owners and delivery drivers.

### 2. Restaurant Ownership & Visibility
- Add `ownerUserId` (or equivalent) to `Restaurant`.
- Enforce in RestaurantController that only the owning `ROLE_RESTAURANT_OWNER` (or `ROLE_ADMIN`) can create/update/delete a restaurant and its menu.
- Ensure only APPROVED restaurants and owners are visible/usable in customer-facing listing and search APIs.

### 3. Order Cancellation Rule
- Add a `POST /api/orders/{id}/cancel` endpoint that:
  - Checks ownership (only the customer who placed the order, or an admin, can cancel).
  - Enforces the ~2-minute cancellation window by comparing `orderTime` with the current time.
  - Restricts cancellation to allowed statuses (e.g., `NEW`, `PROCESSING`).
  - Updates status to `CANCELLED` and returns the updated `OrderResponseDTO`.

### 4. Order Status Transitions
- Define allowed status transitions (e.g., `NEW → PROCESSING → CONFIRMED → OUT_FOR_DELIVERY → DELIVERED/CANCELLED`).
- Implement internal methods/endpoints in order-service to change status according to these rules, ready to be triggered by restaurant actions and (later) a delivery-service.

### 5. Error & Logging Standardization
- Introduce a common error response schema (e.g., `code`, `message`, `details`) across user-, restaurant-, and order-services using `@ControllerAdvice`.
- Standardize logging fields (correlation ID, `userId`, `orderId`, `restaurantId`) across services to prepare for ELK and OpenTelemetry.

---

## Phase 3 – Advanced Capabilities

Focus: delivery, payments, multi‑restaurant support, richer search, and deep observability.

### 1. Delivery-Service & Delivery Fees
- Create a dedicated delivery-service to manage deliveries:
  - Entities for delivery jobs, linking orders with `ROLE_DELIVERY_DRIVER` users.
  - APIs for assignment and driver status updates.
- Implement delivery-fee calculation based on order amount ranges and expose it via delivery-service; integrate fees into order creation and snapshots.

### 2. Payment-Service Contract
- Define payment status within `Order` and an external payment-service API for initiating payments, checking status, and handling refunds.
- Adjust order-service flows so that certain status transitions (e.g., to `CONFIRMED`) depend on successful payment.

### 3. Multi‑Restaurant Carts & Orders
- Extend `CartItem` with `restaurantId` (and optionally `restaurantName`).
- Relax Phase 1 single‑restaurant checks to:
  - Allow multi‑restaurant carts.
  - Either support multi‑restaurant orders or automatically split a cart into multiple single‑restaurant orders at checkout.

### 4. Richer Search & Ranking
- Enhance search to incorporate ranking signals such as popularity, ratings, and promotions in Elasticsearch queries.
- Expose filtering and sorting options (e.g., by rating, distance, offers) via the search API.

### 5. Deep Observability (ELK + OpenTelemetry)
- Wire logs from all services into ELK and create dashboards for key business and technical metrics (GMV, order success rate, search errors, auth failures).
- Add OpenTelemetry instrumentation in api-gateway and core services for distributed tracing, mapping correlation IDs to trace/span IDs and tagging spans with domain attributes (userId, orderId, restaurantId, city).

---

This plan is designed to deliver a usable, secure single‑restaurant MVP quickly, while keeping models, DTOs, and observability patterns ready for multi‑restaurant orders, delivery orchestration, payments, and richer analytics in later phases.
