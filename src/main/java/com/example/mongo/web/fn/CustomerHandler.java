package com.example.mongo.web.fn;

import com.example.mongo.dto.CustomerDTO;
import com.example.mongo.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerHandler {
    private final CustomerService service;
    private final Validator validator;

    private void validate(CustomerDTO dto) {
        Errors errors = new BeanPropertyBindingResult(dto, "customerDto");
        validator.validate(dto, errors);

        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }

    public Mono<ServerResponse> getAll(ServerRequest request) {
        return ServerResponse.ok().body(service.getAll(), CustomerDTO.class);
    }

    public Mono<ServerResponse> getById(ServerRequest request) {
        return ServerResponse.ok().body(service.getById(request.pathVariable("id")), CustomerDTO.class);
    }

    public Mono<ServerResponse> addCustomer(ServerRequest request) {
        return service.addCustomer(request.bodyToMono(CustomerDTO.class).doOnNext(this::validate))
                .flatMap(dto -> ServerResponse
                        .created(UriComponentsBuilder.fromPath(BeerRouterConfig.BEER_PATH_WITH_ID)
                                .build(dto.getId()))
                        .build());
    }

    public Mono<ServerResponse> editCustomer(ServerRequest request) {
        return request.bodyToMono(CustomerDTO.class)
                .doOnNext(this::validate)
                .flatMap(dto -> service.editCustomer(request.pathVariable("id"), dto)
                        .flatMap(saved -> ServerResponse.noContent().build()));
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
        return service.deleteCustomer(request.pathVariable("id"))
                .then(ServerResponse.noContent().build());
    }
}
