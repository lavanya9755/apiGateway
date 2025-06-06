
# 🌀 Spring API Gateway Routing (Functional Style)

This project demonstrates how to configure **functional routing** in Spring Boot to build a **basic API Gateway** that forwards incoming HTTP requests to specific microservices like Product, Order, or Inventory services.

---

## 📁 What’s Inside?

This example shows how to define a route for the **Product Service** using Spring WebFlux-style routing with a custom (non-standard) DSL.

```java
@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> productServiceRouter(){
        return GatewayRouterFunctions.route("productservice")
                .route(RequestPredicates.path("/api/product"), HandlerFunctions.http("http://localhost:8080"))
                .build();
    }
}
````

---

## 🧠 How It Works (Step-by-Step)

### 1. `@Configuration`

Marks the class as a configuration class that Spring will scan and load at startup.

### 2. `@Bean`

Registers a Spring bean of type `RouterFunction<ServerResponse>`, which defines how HTTP requests should be handled.

### 3. Routing Logic

```java
GatewayRouterFunctions.route("productservice")
```

Starts routing logic for a group called `productservice`.

```java
.route(RequestPredicates.path("/api/product"), HandlerFunctions.http("http://localhost:8080"))
```

This means:

* If someone hits the API Gateway at `/api/product`,
* The request will be forwarded to the **Product Service**, which is assumed to be running locally at `http://localhost:8080`.

```java
.build()
```

Finalizes and builds the routing function.

---

## 🚀 Example

Suppose your **API Gateway** is running at `http://localhost:8888`. If a client sends a request to:

```
http://localhost:8888/api/product
```

It will be internally routed to:

```
http://localhost:8080
```

This makes it easier to hide backend service URLs from the clients and centralize routing logic.

---

## ❗ Note

* `GatewayRouterFunctions` and `HandlerFunctions.http()` are **non-standard** Spring APIs. They likely come from a custom or 3rd-party routing DSL.
* This approach is **code-based routing** (functional style) instead of the typical `application.yml` config used in **Spring Cloud Gateway**.
* This is ideal if you prefer type-safe routing defined in Java instead of external config files.

---

## ✅ When to Use This

* You are building an API Gateway in Spring and want full control in Java code.
* You have a few routes and want to define them explicitly.
* You're experimenting with custom Spring WebFlux-based routers.
---

# ✅ 1. **What is Circuit Breaker using Resilience4j in Spring Boot?**

### 🔌 Problem it solves:
When a **remote service** (like inventory or payment) is down or slow, your app will:
* Keep calling it repeatedly.
* Waste resources.
* Cause **cascading failures**.

### 🛡️ Solution: Circuit Breaker Pattern
It "wraps" remote calls and monitors failures.
* If the failures cross a threshold ➝ it **opens the circuit** and stops calling the broken service temporarily.
* After a wait, it **half-opens** to test if the service is back.
* If healthy, it **closes** again.

---

### 🔧 In Spring Boot (with Resilience4j)

You just annotate your method:

```java
@CircuitBreaker(name = "inventoryService", fallbackMethod = "fallbackInventory")
public ResponseEntity<?> callInventoryService() {
    return restTemplate.getForEntity("http://inventory/api/check", String.class);
}

public ResponseEntity<?> fallbackInventory(Exception ex) {
    return ResponseEntity.ok("Inventory service is down. Please try later.");
}
```

---

## ✅ 2. **Explanation of application.properties config code (line-by-line)**

---

### 🔹 **Actuator Endpoints**
```properties
management.health.circuitbreakers.enabled=true
```
➡ Enables Circuit Breaker metrics in the `/actuator/health` endpoint.
```properties
management.endpoints.web.exposure.include=*
```
➡ Exposes **all actuator endpoints** (like `/actuator/health`, `/actuator/circuitbreakers`).
```properties
management.endpoint.health.show-details=always
```
➡ Shows full health info in actuator responses (not just “UP/DOWN”).

---

### 🔹 **Resilience4j Circuit Breaker Config**

```properties
resilience4j.circuitbreaker.configs.default.registerHealthIndicator=true
```

➡ Registers health status of each circuit breaker with Spring Boot Actuator.

```properties
resilience4j.circuitbreaker.configs.default.slidingWindowType=COUNT_BASED
```

➡ Sliding window counts **number of calls** (instead of time-based window).

```properties
resilience4j.circuitbreaker.configs.default.slidingWindowSize=10
```

➡ Tracks last **10 calls** to check failure rate.

```properties
resilience4j.circuitbreaker.configs.default.failureRateThreshold=50
```

➡ If **50% or more** of last 10 calls failed, open the circuit.

```properties
resilience4j.circuitbreaker.configs.default.waitDurationInOpenState=5s
```

➡ Once open, wait **5 seconds** before transitioning to half-open (test state).

```properties
resilience4j.circuitbreaker.configs.default.permittedNumberOfCallsInHalfOpenState=3
```

➡ Allow only **3 test calls** in half-open state to decide whether to close the circuit again.

```properties
resilience4j.circuitbreaker.configs.default.automaticTransitionFromOpenToHalfOpenEnabled=true
```

➡ Automatically move from **open → half-open** after wait duration (instead of requiring a manual trigger).

---

### 🔹 **Timeout Settings**

```properties
resilience4j.timelimiter.configs.default.timeout-duration=3s
```

➡ Any remote call exceeding **3 seconds** will be considered a **timeout failure**.

---

### ✅ Summary

This config:

* Makes your microservice **resilient** to slow/downstream failures.
* Shows status in **actuator** for monitoring.
* Controls how many failures open the circuit, how long to wait, how to retry, and when to recover.

---

## 📦 Requirements

* Java 17+
* Spring Boot 3+
* Possibly a custom dependency that provides `GatewayRouterFunctions` and `HandlerFunctions.http()` (make sure it's on your classpath)

---

## 🛠 Future Improvements

* Add more services like Order and Inventory
* Implement error handling
* Add authentication (JWT-based or OAuth2) at the gateway level
* Replace functional DSL with official Spring Cloud Gateway if needed

---

## 📚 Learn More

* [Spring WebFlux Functional Endpoints](https://docs.spring.io/spring-framework/reference/web/webflux/fn.html)
* [Spring Cloud Gateway (Official)](https://spring.io/projects/spring-cloud-gateway)

---
