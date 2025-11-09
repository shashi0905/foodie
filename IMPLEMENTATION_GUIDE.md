# Foodie Microservices - Implementation Guide

## User Service & API Gateway Implementation Tutorial

This guide provides step-by-step instructions for implementing User Service and API Gateway in the Foodie microservices application.

---

## Table of Contents
1. [User Service Implementation](#user-service-implementation)
2. [API Gateway Implementation](#api-gateway-implementation)
3. [Running the Services](#running-the-services)
4. [Testing the Implementation](#testing-the-implementation)
5. [Architecture Overview](#architecture-overview)

---

## User Service Implementation

### Step 1: Project Setup

**Create directory structure:**
```bash
mkdir -p user-service/src/main/java/com/shashi/foodie/userservice
mkdir -p user-service/src/main/resources
```

### Step 2: Add Dependencies (pom.xml)

Key dependencies for User Service:
- `spring-boot-starter-web` - REST API support
- `spring-boot-starter-data-jpa` - Database operations
- `spring-boot-starter-security` - Security framework
- `spring-cloud-starter-netflix-eureka-client` - Service discovery
- `jjwt` (v0.12.6) - JWT token generation/validation
- `postgresql` - Database driver

### Step 3: Configure Application Properties

```properties
# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/foodie_users
spring.datasource.username=postgres
spring.datasource.password=password

# JPA
spring.jpa.hibernate.ddl-auto=update

# Server
server.port=8081

# Eureka
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka

# JWT
jwt.secret=<your-secret-key>
jwt.expiration=86400000
```

### Step 4: Create Domain Models

**User Entity** (`model/User.java`):
- Fields: id, email, password, firstName, lastName, phoneNumber
- Relationships: Many-to-Many with Role, One-to-Many with Address
- Annotations: @Entity, @Table, timestamps

**Role Entity** (`model/Role.java`):
- Enum: ROLE_CUSTOMER, ROLE_RESTAURANT_OWNER, ROLE_DELIVERY_DRIVER, ROLE_ADMIN
- Stored as String in database

**Address Entity** (`model/Address.java`):
- Fields: street, city, state, zipCode, country, landmark
- Relationship: Many-to-One with User

### Step 5: Create DTOs

**RegisterRequest**: Email, password, firstName, lastName, phoneNumber
**LoginRequest**: Email, password
**AuthResponse**: Token, user details, roles
**UserDTO**: User information without password

### Step 6: Create Repository Layer

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Role.RoleType name);
}
```

### Step 7: Implement Security

**JWT Utilities** (`security/JwtUtil.java`):
- `generateToken()` - Create JWT from authentication
- `getUsernameFromToken()` - Extract username from token
- `validateToken()` - Verify token validity

**UserDetailsImpl** - Implements Spring Security UserDetails

**JwtAuthenticationFilter** - Intercepts requests, validates JWT

**SecurityConfig**:
- Password encoder (BCrypt)
- Authentication provider
- Security filter chain
- Permit: `/api/auth/**`, Authenticate: all other endpoints

### Step 8: Create Service Layer

**AuthService Interface**:
- `register()` - User registration
- `login()` - User authentication
- `getUserProfile()` - Get user details

**AuthServiceImpl**:
- Password encoding
- Role assignment (default: CUSTOMER)
- JWT token generation
- Exception handling

### Step 9: Create Controllers

**AuthController** (`/api/auth`):
- POST `/register` - User registration
- POST `/login` - User login

**UserController** (`/api/users`):
- GET `/profile` - Get current user profile
- GET `/{userId}` - Get user by ID

### Step 10: Initialize Data

**DataInitializer** - CommandLineRunner to seed default roles on startup

---

## API Gateway Implementation

### Step 1: Project Setup

**Create directory structure:**
```bash
mkdir -p api-gateway/src/main/java/com/shashi/foodie/apigateway
mkdir -p api-gateway/src/main/resources
```

### Step 2: Add Dependencies (pom.xml)

Key dependencies:
- `spring-cloud-starter-gateway` - Gateway framework
- `spring-cloud-starter-netflix-eureka-client` - Service discovery
- `spring-boot-starter-actuator` - Monitoring

### Step 3: Configure Routes (application.properties)

```properties
server.port=8080

# Route 1: User Service
spring.cloud.gateway.routes[0].id=user-service
spring.cloud.gateway.routes[0].uri=lb://user-service
spring.cloud.gateway.routes[0].predicates[0]=Path=/api/auth/**, /api/users/**

# Route 2: Order Service
spring.cloud.gateway.routes[1].id=order-service
spring.cloud.gateway.routes[1].uri=lb://order-service
spring.cloud.gateway.routes[1].predicates[0]=Path=/api/orders/**

# Route 3: Restaurant Service
spring.cloud.gateway.routes[2].id=restaurant-service
spring.cloud.gateway.routes[2].uri=lb://restaurant-service
spring.cloud.gateway.routes[2].predicates[0]=Path=/api/restaurants/**, /api/search/**, /api/cart/**

# Enable discovery
spring.cloud.gateway.discovery.locator.enabled=true
```

**Route Components:**
- `id` - Unique route identifier
- `uri` - Target service (lb:// for load balancing via Eureka)
- `predicates` - Path matching patterns

### Step 4: Create Main Application Class

```java
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
```

### Step 5: Add Global Filters (Optional)

**LoggingFilter** - Logs incoming requests and responses

---

## Running the Services

### Prerequisites

1. **PostgreSQL** - Database for User Service
   ```bash
   # Create database
   createdb foodie_users
   ```

2. **MySQL** - Database for Restaurant Service (already configured)

3. **Elasticsearch** - Search for Restaurant Service (already configured)

### Startup Order

```bash
# 1. Start Service Registry (Eureka)
cd service-registry
mvn spring-boot:run

# 2. Start User Service
cd user-service
mvn spring-boot:run

# 3. Start Restaurant Service
cd restaurant-service
mvn spring-boot:run

# 4. Start Order Service
cd order-service
mvn spring-boot:run

# 5. Start API Gateway
cd api-gateway
mvn spring-boot:run
```

**Service Ports:**
- Service Registry: 8761
- API Gateway: 8080
- User Service: 8081
- Restaurant Service: Dynamic (registered with Eureka)
- Order Service: Dynamic (registered with Eureka)

---

## Testing the Implementation

### 1. Check Service Registry
```
http://localhost:8761
```
All services should be registered.

### 2. Register a New User

**Request:**
```bash
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "phoneNumber": "1234567890"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "user@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "roles": ["ROLE_CUSTOMER"]
}
```

### 3. Login

**Request:**
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

### 4. Get User Profile (Authenticated)

**Request:**
```bash
GET http://localhost:8080/api/users/profile
Authorization: Bearer <token>
```

### 5. Test Other Services Through Gateway

**Get Restaurants:**
```bash
GET http://localhost:8080/api/restaurants
```

**Create Order:**
```bash
POST http://localhost:8080/api/orders
Authorization: Bearer <token>
Content-Type: application/json

{
  "customerId": 1,
  "restaurantId": 1,
  "items": [...]
}
```

---

## Architecture Overview

### Request Flow

```
Client → API Gateway (8080) → Service Registry (8761) → Microservices
```

1. **Client** sends request to API Gateway
2. **API Gateway** matches request path to route
3. **Gateway** queries Eureka for service instance
4. **Gateway** forwards request to target service
5. **Service** processes and returns response
6. **Gateway** returns response to client

### Security Flow

```
Client → Gateway → User Service → JWT Token → Client
Client → Gateway (with JWT) → Any Service (validated)
```

1. User registers/logs in via User Service
2. User Service returns JWT token
3. Client includes token in subsequent requests
4. Each service can validate token independently

### Service Communication

- **Synchronous**: REST via API Gateway
- **Load Balancing**: Eureka + Ribbon (built into Spring Cloud)
- **Service Discovery**: Automatic via Eureka

---

## Key Design Patterns

1. **API Gateway Pattern** - Single entry point for all clients
2. **Service Registry Pattern** - Dynamic service discovery
3. **Authentication Pattern** - JWT token-based security
4. **Repository Pattern** - Data access abstraction
5. **DTO Pattern** - Data transfer between layers

---

## Next Steps

1. **Add Circuit Breaker** - Resilience4j for fault tolerance
2. **Add Rate Limiting** - Protect services from overload
3. **Implement Refresh Tokens** - Better session management
4. **Add Swagger Documentation** - API documentation
5. **Implement Password Reset** - Email-based password recovery
6. **Add User Roles Management** - Admin endpoints
7. **Implement Address CRUD** - Delivery address management

---

## Troubleshooting

### Service Not Registering with Eureka
- Check `eureka.client.serviceUrl.defaultZone` configuration
- Ensure Service Registry is running
- Verify network connectivity

### JWT Token Errors
- Check `jwt.secret` configuration
- Ensure token is passed in `Authorization: Bearer <token>` header
- Verify token hasn't expired

### Gateway Routing Issues
- Check route predicates match request path
- Verify service is registered in Eureka
- Check Gateway logs for routing decisions

### Database Connection Issues
- Verify PostgreSQL is running
- Check database credentials
- Ensure database exists

---

## Configuration Summary

| Service | Port | Database | Key Features |
|---------|------|----------|--------------|
| Service Registry | 8761 | None | Eureka Server |
| API Gateway | 8080 | None | Routing, Load Balancing |
| User Service | 8081 | PostgreSQL | Authentication, JWT |
| Restaurant Service | Dynamic | MySQL, Elasticsearch | Restaurants, Search, Cart |
| Order Service | Dynamic | H2 (In-Memory) | Order Management |

---

## Useful Commands

```bash
# Build all services
mvn clean install

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Check running services
curl http://localhost:8761/eureka/apps

# Test API Gateway routes
curl http://localhost:8080/actuator/gateway/routes
```

---

## References

- [Spring Cloud Gateway Docs](https://spring.io/projects/spring-cloud-gateway)
- [Spring Security JWT](https://jwt.io/)
- [Netflix Eureka](https://github.com/Netflix/eureka/wiki)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
