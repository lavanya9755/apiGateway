
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
