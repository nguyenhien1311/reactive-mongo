package com.example.mongo.service;

import com.example.mongo.domain.Beer;
import com.example.mongo.dto.BeerDTO;
import com.example.mongo.mappers.BeerMapper;
import com.example.mongo.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {
    private final BeerRepository repository;
    private final BeerMapper mapper;

    @Override
    public Flux<BeerDTO> getAll() {
        return repository.findAll().map(mapper::toDto);
    }

    @Override
    public Flux<BeerDTO> getAllByBeerStyle(String beerStyle) {
        return repository.findAllByBeerStyle(beerStyle).map(mapper::toDto);
    }

    @Override
    public Mono<BeerDTO> getByName(String beerName) {
        return repository.findByBeerName(beerName).map(mapper::toDto).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)));
    }

    @Override
    public Mono<BeerDTO> editBeer(String id, BeerDTO dto) {
        return checkIfExist(id).map(found -> {
                    found.setBeerName(dto.getBeerName());
                    found.setBeerStyle(dto.getBeerStyle());
                    found.setPrice(dto.getPrice());
                    found.setQuantityOnHand(dto.getQuantityOnHand());
                    found.setUpc(dto.getUpc());
                    return found;
                }).flatMap(repository::save)
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> deleteBeer(String id) {
        return checkIfExist(id).flatMap(repository::delete);
    }

    @Override
    public Mono<BeerDTO> addBeer(Mono<BeerDTO> dto) {
        return dto.map(mapper::toDomain)
                .flatMap(repository::save)
                .map(mapper::toDto);
    }

    @Override
    public Mono<BeerDTO> getById(String id) {
        return checkIfExist(id)
                .map(mapper::toDto);
    }

    private Mono<Beer> checkIfExist(String id) {
        return repository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)));
    }
}
