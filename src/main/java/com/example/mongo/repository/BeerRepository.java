package com.example.mongo.repository;

import com.example.mongo.domain.Beer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerRepository extends ReactiveMongoRepository<Beer, String> {
    Mono<Beer> findByBeerName(String beerName);

    Flux<Beer> findAllByBeerStyle(String beerStyle);
}
