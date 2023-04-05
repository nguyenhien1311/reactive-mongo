package com.example.mongo.web.fn;

import com.example.mongo.dto.BeerDTO;
import com.example.mongo.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BeerHandler {

    private final BeerService service;

    private final Validator validator;

    private void validate(BeerDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "beerDto");
        validator.validate(dto, errors);

        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> getBeerById(ServerRequest request) {
        return ServerResponse
                .ok()
                .body(service.getById(request.pathVariable("id")), BeerDTO.class);
    }

    public Mono<ServerResponse> listBeers(ServerRequest request) {
        Flux<BeerDTO> flux;

        if (request.queryParam("beerStyle").isPresent()) {
            flux = service.getAllByBeerStyle(request.queryParam("beerStyle").get());
        } else {
            flux = service.getAll();
        }
        return ServerResponse
                .ok()
                .body(flux, BeerDTO.class);
    }

    public Mono<ServerResponse> addBeer(ServerRequest request) {
        return service.addBeer(request.bodyToMono(BeerDTO.class).doOnNext(this::validate))
                .flatMap(dto -> ServerResponse
                        .created(UriComponentsBuilder
                                .fromPath(BeerRouterConfig.BEER_PATH)
                                .build(dto.getId()))
                        .build());
    }

    public Mono<ServerResponse> editBeer(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
                .doOnNext(this::validate)
                .flatMap(dto -> service
                        .editBeer(request.pathVariable("id"), dto)
                        .flatMap(saved -> ServerResponse.noContent().build()));
    }

    public Mono<ServerResponse> deleteBeer(ServerRequest request) {
        return service.deleteBeer(request.pathVariable("id"))
                .then(ServerResponse.noContent().build());
    }
}
