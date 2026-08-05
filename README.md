<<<<<<< HEAD
# Microservices Demo (No Docker) — Eureka + API Gateway + Feign

4 independent Spring Boot Maven projects. Run each with plain `mvn spring-boot:run`
(or build a jar with `mvn clean package` and `java -jar target/*.jar`).

Requires: Java 17+, Maven 3.8+, internet access (to download dependencies once).

## Services

| Service         | Port | Role                                                             |
|-----------------|------|-------------------------------------------------------------------|
| eureka-server   | 8761 | Service registry — every service registers here                  |
| api-gateway     | 8080 | Single entry point, routes requests to services via Eureka        |
| product-service | 8082 | Owns Product data (H2 in-memory DB)                                |
| user-service    | 8081 | Owns User data, calls product-service using a **Feign Client**    |

## Start order (important)

Start in this order, waiting ~10-15s between each so services register in Eureka:

```bash
# 1. Eureka Server
cd eureka-server
mvn spring-boot:run

# 2. Product Service (new terminal)
cd product-service
mvn spring-boot:run

# 3. User Service (new terminal)
cd user-service
mvn spring-boot:run

# 4. API Gateway (new terminal)
cd api-gateway
mvn spring-boot:run
```

Check the Eureka dashboard at: http://localhost:8761
You should see PRODUCT-SERVICE, USER-SERVICE and API-GATEWAY registered.

## How the Feign call works

`user-service` has a Feign client interface:

```java
@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/api/products/{id}")
    ProductDTO getProductById(@PathVariable("id") Long id);
}
```

`"product-service"` is not a URL — it's the `spring.application.name` that
product-service registered in Eureka. Feign + Spring Cloud LoadBalancer resolve
that name to an actual `host:port` at runtime, so user-service never
hardcodes product-service's address.

## Test it — direct service calls

```bash
# Product service directly
curl http://localhost:8082/api/products
curl http://localhost:8082/api/products/1

# User service directly
curl http://localhost:8081/api/users
curl http://localhost:8081/api/users/1

# The interesting one: user-service calls product-service internally via Feign
curl http://localhost:8081/api/users/1/with-product
```

Expected response for the last call (combines User + Product data fetched live
from product-service):

```json
{
  "userId": 1,
  "name": "Alice Sharma",
  "email": "alice@example.com",
  "product": {
    "id": 1,
    "name": "Laptop",
    "description": "15 inch, 16GB RAM",
    "price": 899.99,
    "quantity": 25
  }
}
```

## Test it — through the API Gateway (recommended)

Everything is also reachable through the single Gateway port 8080:

```bash
curl http://localhost:8080/api/products
curl http://localhost:8080/api/products/1
curl http://localhost:8080/api/users
curl http://localhost:8080/api/users/1/with-product
```

The gateway routes `/api/products/**` → product-service and
`/api/users/**` → user-service, both resolved dynamically via Eureka
(`lb://product-service`, `lb://user-service` in api-gateway's application.yml).

## Create data

```bash
# Add a product
curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Monitor","description":"27 inch 4K","price":329.99,"quantity":40}'

# Add a user pointing at product id 4 (the one just created)
curl -X POST http://localhost:8081/api/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Deepa Nair","email":"deepa@example.com","productId":4}'

curl http://localhost:8081/api/users/4/with-product
```

## What happens if product-service is down?

Stop product-service and call `/api/users/1/with-product` again — user-service's
Feign call fails and the endpoint returns HTTP 503 with an error message
instead of crashing, because the call is wrapped in a try/catch around
`FeignException` in `UserService.getUserWithProduct()`.

## H2 Consoles (for inspecting data)

- product-service: http://localhost:8082/h2-console (JDBC URL: `jdbc:h2:mem:productdb`)
- user-service: http://localhost:8081/h2-console (JDBC URL: `jdbc:h2:mem:userdb`)
(username `sa`, empty password)
=======
# microservices
Distributed system
>>>>>>> 164911a6fcb35e96de4739f129418097ea326a11
