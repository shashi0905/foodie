# Business Context Document

## 1. Why This System Exists
- Provide an end-to-end food delivery marketplace connecting customers, restaurants, and delivery drivers within a city.
- Offer a unified digital experience for discovery, ordering, and fulfillment of food orders via a microservices-based backend.
- Enable the platform to manage users, restaurant inventory, orders, carts, and delivery orchestration in a scalable, extensible way.

## 2. Business Problem & Impact
- **For customers:** Difficulty discovering relevant food options quickly and reliably ordering from multiple restaurants. Without this system, they rely on fragmented channels (phone calls, individual apps, in-person ordering).
- **For restaurants:** Limited digital reach and operational tooling to receive, manage, and track online orders in a standardized way.
- **For delivery agents:** Lack of a consolidated stream of delivery jobs and routing logic tied to restaurant and customer locations.
- **For the platform:** Need a coherent backend to support a commission-based marketplace, with room to add promotions, delivery pricing logic, and city expansion.

If the system is unavailable or unreliable:
- Orders cannot be placed or tracked, directly impacting GMV and commission revenue.
- Restaurants lose online sales and may lose trust in the platform.
- Customer experience degrades, harming retention and word-of-mouth.

## 3. Users & Stakeholders
- **Customers (ROLE_CUSTOMER)**
  - Browse and search restaurants and food items.
  - Manage carts and place orders.
  - Eventually track order status and delivery.
- **Restaurant Owners (ROLE_RESTAURANT_OWNER)**
  - Represent real-world restaurants operating on the platform.
  - Own and maintain restaurant data and menus (Restaurant, FoodItem) via restaurant-service.
  - Fulfill incoming orders sent from order-service.
  - Require an approval/onboarding process managed by admins.
- **Delivery Drivers (ROLE_DELIVERY_DRIVER)**
  - Delivery agents responsible for transporting food from restaurants to customers.
  - Will be managed by the platform (delivery is platform-owned, not purely restaurant self-delivery).
  - Future services will assign deliveries based on factors like distance and agent workload/history.
- **Admins (ROLE_ADMIN)**
  - Manage platform-wide concerns: approving restaurant and driver accounts, overseeing operations, and enforcing policies.
  - Gatekeeper for restaurant and driver onboarding flows.
- **Internal stakeholders** (non-code roles)
  - Product & ops teams: define policies for cancellation, refunds, delivery strategies, and expansion.
  - Finance: track commission revenue, payouts, and fees.
  - Support: handle customer and restaurant escalations.

## 4. Value Creation Points
- **Commission on order total**
  - Primary monetization mechanism in the current design: the platform earns a commission based on the order total.
- **Delivery fee (via future delivery service)**
  - Additional revenue/fee layer determined by a delivery-service using order amount ranges (e.g., tiered fees with free delivery beyond a threshold such as ₹500).
- **Enhanced discovery and conversion**
  - Restaurant-service plus Elasticsearch-backed search-service improve restaurant and item discoverability, directly influencing conversion rates.
- **Operational efficiency for restaurants**
  - Standardized order intake and menu management; reduces friction compared to manual or fragmented solutions.
- **Future levers (planned, not yet implemented)**
  - Promotions, coupons, and restaurant-specific discounts to influence demand and retention.
  - Potential paid visibility or ranking promotions (to be defined) leveraging search and discovery.

## 5. Core Business Domains
- **Identity & Access (user-service)**
  - Manages Users, Roles (customer, restaurant owner, delivery driver, admin), authentication (JWT), and profiles/addresses.
  - Distinguishes self-serve customer registration from admin-approved restaurant owner and driver onboarding.
- **Restaurant & Menu Management (restaurant-service)**
  - Source of truth for Restaurant and FoodItem entities and their relationships.
  - Exposes APIs for listing restaurants and their menus.
- **Search & Discovery (search-service within restaurant-service)**
  - Provides keyword-based search on FoodItem using Elasticsearch.
  - Will evolve to include ranking and relevance strategies that can incorporate popularity, rating, promotions, etc.
- **Cart & Pre-Order Experience (restaurant-service)**
  - Cart and CartItem models and APIs to add/remove items and fetch a customer’s cart.
  - Represents the pre-checkout “basket” before an order is created.
- **Order Management (order-service)**
  - Owns Order, OrderItem, and Status lifecycle (NEW, PROCESSING, CONFIRMED, OUT_FOR_DELIVERY, DELIVERED, CANCELLED).
  - Interfaces with restaurant-service to validate and fulfill orders.
  - Will integrate with payment and delivery services in later phases.
- **API Aggregation & Routing (api-gateway + service-registry)**
  - Provides a single API surface for clients (`/api/auth`, `/api/users`, `/api/orders`, `/api/restaurants`, `/api/search`, `/api/cart`).
  - Uses service discovery (Eureka) and gateway routing to reach underlying microservices.

## 6. Critical Business Workflows
- **Customer onboarding & authentication**
  - Customer registers via `/api/auth/register`.
  - Basic validation is performed; email/phone validation is expected in future iterations.
  - Customer logs in via `/api/auth/login` and receives JWT for subsequent interactions.
- **Restaurant owner & driver onboarding**
  - Prospective restaurant owners and delivery drivers create accounts.
  - Admins must approve these accounts before they can operate on the platform (approval workflows still to be implemented in code).
- **Restaurant and menu management**
  - Approved restaurant owners manage their restaurant profile and menu items in restaurant-service (currently via backend APIs; future UIs assumed).
  - Restaurant-service remains the master for menus and pricing; order-service relies on it to fetch up-to-date menu details.
- **Browse, search, and cart**
  - Customers browse restaurants via `/api/restaurants` and fetch menus via `/api/restaurants/{id}/food-items`.
  - Customers search for food items using `/api/search?keyword=...`, powered by Elasticsearch and FoodItemSearchRepository.
  - Customers add/remove items in their cart via `/api/cart` endpoints.
- **Checkout and order creation**
  - When a customer chooses to “place order,” order-service creates an Order from the cart data (conceptually: cart → order), using DTOs such as OrderRequestDTO.
  - Order-service is expected to call restaurant-service to validate and coordinate fulfillment of the ordered items.
  - Payment is not yet implemented; a dedicated payment service is planned for a later phase.
- **Order lifecycle & delivery (target state)**
  - Order transitions through statuses: NEW → PROCESSING → CONFIRMED → OUT_FOR_DELIVERY → DELIVERED (or CANCELLED where allowed).
  - Platform-owned delivery assigns a delivery driver based on restaurant and customer locations and strategies such as distance and driver workload/history (to be implemented in a delivery service).
  - Delivery fees will be determined by a delivery-service using order amount ranges and free-delivery thresholds.

## 7. Business Rules & Constraints
- **Role-based responsibilities**
  - Customers may self-register and immediately browse and place orders once authenticated.
  - Restaurant owners and delivery drivers require admin approval before they are considered active on the platform.
- **Cancellation policy**
  - An order can only be cancelled within a short grace period (e.g., within 2 minutes of order placement).
  - Within this window, the customer is eligible for a full refund.
  - After the grace period elapses, orders are not eligible for cancellation.
- **Pricing & fees (current and target)**
  - Platform earns a commission on the order total.
  - Delivery fee tiers and free-delivery thresholds are defined by the delivery service logic (e.g., amount ranges such as ₹1–200, ₹200–400, and free delivery above a certain value).
- **Source of truth for restaurants/menus**
  - Restaurant-service is the canonical source of truth for restaurants and menu items (Restaurant, FoodItem).
  - Order-service is expected to query restaurant-service, not any external mock, in real deployments.
- **Geographic scope (initial)**
  - Initial target scope is a single city.
  - Architecture and business design should not preclude later multi-city or multi-region expansion.

## 8. Success Metrics
- **Customer-side metrics**
  - Conversion rate from search/browse sessions to placed orders.
  - Cart abandonment rate and time-to-first-order for new customers.
  - Average order rating / customer satisfaction (to be added when ratings are implemented).
- **Restaurant-side metrics**
  - Number of active restaurants and their monthly order volumes.
  - Restaurant retention and churn.
- **Platform & financial metrics**
  - Gross merchandise value (GMV) processed through the platform.
  - Commission revenue generated from orders.
  - Delivery fee revenue (once delivery-service and fee logic are live).
- **Operational & reliability metrics**
  - Order success rate (orders that reach DELIVERED vs those that fail or are cancelled within the allowed window).
  - Latency and error rates for critical APIs (`/api/orders`, `/api/restaurants`, `/api/search`).

## 9. Explicit Non-Goals (Current Phase)
- **No payment processing yet**
  - Payment flows, gateways, and refund mechanics are out of scope for the current implementation phase and will be handled by a future payment service.
- **No advanced promotion/loyalty system yet**
  - Coupons, loyalty programs, and complex discounting are future enhancements; only the conceptual scope exists currently.
- **No full-featured delivery orchestration yet**
  - Although `ROLE_DELIVERY_DRIVER` exists, driver assignment algorithms, routing, and tracking are not implemented.
- **No multi-city complexity in current release**
  - While future expansion is anticipated, current logic assumes operation in a single city without region-aware routing or pricing.

## 10. Assumptions & Open Questions
- **Assumptions (based on current understanding)**
  - Platform commission is applied uniformly across restaurants for now (no restaurant-specific commission tiers yet).
  - Refund processing, while conceptually aligned with the 2-minute cancellation rule, will be handled by the future payment service.
  - Search ranking will eventually incorporate non-textual signals (popularity, rating, promotions), but current implementation is primarily keyword-based.
  - Delivery-service will be a separate microservice owning delivery-fee logic and driver assignment strategies.
- **Open questions for future clarification**
  - Exact commission percentage(s) and whether they vary by restaurant, cuisine, or geography.
  - Detailed dispute and refund handling rules when restaurants fail to fulfill confirmed orders or when delivery fails late in the lifecycle.
  - Whether there will be any subscription or SaaS-style pricing for restaurants in addition to per-order commission.
  - Compliance and regulatory constraints (e.g., data residency, tax handling, invoicing formats) for future multi-city or multi-country expansion.
