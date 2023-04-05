package com.example.mongo.web.fn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class CustomerRouterConfig {
    private final CustomerHandler handler;
    public static final String CUSTOMER_PATH = "/api/v3/customers";
    public static final String CUSTOMER_PATH_WITH_ID = CUSTOMER_PATH + "/{id}";

    @Bean
    public RouterFunction<ServerResponse> customerRoutes() {
        return route()
                .GET(CUSTOMER_PATH, accept(MediaType.APPLICATION_JSON), handler::getAll)
                .GET(CUSTOMER_PATH_WITH_ID, accept(MediaType.APPLICATION_JSON), handler::getById)
                .POST(CUSTOMER_PATH, accept(MediaType.APPLICATION_JSON), handler::addCustomer)
                .PUT(CUSTOMER_PATH_WITH_ID, accept(MediaType.APPLICATION_JSON), handler::editCustomer)
                .DELETE(CUSTOMER_PATH_WITH_ID, accept(MediaType.APPLICATION_JSON), handler::deleteCustomer)
                .build();
    }
}
