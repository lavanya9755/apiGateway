package com.gateway.apigateway.Routes;

import java.net.URI;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> productServiceRouter() {
        return GatewayRouterFunctions.route("productservice")
                .route(RequestPredicates.path("/api/product/**"), HandlerFunctions.http("http://localhost:8084"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRouter() {
        return GatewayRouterFunctions.route("orderservice")
                .route(RequestPredicates.path("/api/order"), HandlerFunctions.http("http://localhost:8085"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRouter() {
        return GatewayRouterFunctions.route("inventoryservice")
                .route(RequestPredicates.path("/api/inventory"), HandlerFunctions.http("http://localhost:8086"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service is unavailable , thodi der rukjao amazon nh chale jana XOXO"))
                .build();
    }
}
