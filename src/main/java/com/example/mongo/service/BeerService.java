package com.example.mongo.service;

import com.example.mongo.dto.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {
    Flux<BeerDTO> getAll();
    Flux<BeerDTO> getAllByBeerStyle(String beerStyle);
    Mono<BeerDTO> getByName(String beerName);

    Mono<BeerDTO> editBeer(String id, BeerDTO dto);

    Mono<Void> deleteBeer(String id);

    Mono<BeerDTO> addBeer(Mono<BeerDTO> dto);

    Mono<BeerDTO> getById(String id);
}
