package com.pm.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @org.springframework.context.annotation.Bean
    public org.springframework.cloud.gateway.route.RouteLocator customRouteLocator(org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder builder) {
        return builder.routes()
                .route("product-service", r -> r.path("/api/product/**")
                        .uri("lb://product-service"))
                .route("order-service", r -> r.path("/api/order/**")
                        .uri("lb://order-service"))
                .route("discovery-server", r -> r.path("/eureka/web")
                        .filters(f -> f.setPath("/"))
                        .uri("http://localhost:8761"))
                .route("discovery-server-static", r -> r.path("/eureka/**")
                        .uri("http://localhost:8761"))
                .build();
    }

}
