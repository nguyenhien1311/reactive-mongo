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
public class BeerRouterConfig {
    public static final String BEER_PATH = "/api/v3/beers";
    public static final String BEER_PATH_WITH_ID = BEER_PATH + "/{id}";
    private final BeerHandler handler;

    @Bean
    public RouterFunction<ServerResponse> beerRoutes() {
        return route()
                .GET(BEER_PATH, accept(MediaType.APPLICATION_JSON), handler::listBeers)
                .GET(BEER_PATH_WITH_ID, accept(MediaType.APPLICATION_JSON), handler::getBeerById)
                .POST(BEER_PATH, accept(MediaType.APPLICATION_JSON), handler::addBeer)
                .PUT(BEER_PATH_WITH_ID, accept(MediaType.APPLICATION_JSON), handler::editBeer)
                .DELETE(BEER_PATH_WITH_ID, accept(MediaType.APPLICATION_JSON), handler::deleteBeer)
                .build();
    }
}

