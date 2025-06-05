package com.gateway.apigateway.Routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> productServiceRouter() {
        return GatewayRouterFunctions.route("productservice")
        .route(RequestPredicates.path("/api/product/**"), HandlerFunctions.http("http://localhost:8084"))
        .build();
    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRouter() {
        return GatewayRouterFunctions.route("orderservice")
                .route(RequestPredicates.path("/api/order"), HandlerFunctions.http("http://localhost:8085"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRouter() {
        return GatewayRouterFunctions.route("inventoryservice")
                .route(RequestPredicates.path("/api/inventory"), HandlerFunctions.http("http://localhost:8086"))
                .build();
    }
}
