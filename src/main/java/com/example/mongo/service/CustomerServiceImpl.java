package com.example.mongo.service;

import com.example.mongo.domain.Customer;
import com.example.mongo.dto.CustomerDTO;
import com.example.mongo.mappers.CustomerMapper;
import com.example.mongo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @Override
    public Flux<CustomerDTO> getAll() {
        return repository.findAll().map(mapper::toDto);
    }

    @Override
    public Mono<CustomerDTO> getById(String id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public Mono<CustomerDTO> addCustomer(Mono<CustomerDTO> dto) {
        return dto.map(mapper::toDomain).flatMap(repository::save).map(mapper::toDto);
    }

    @Override
    public Mono<CustomerDTO> editCustomer(String id, CustomerDTO dto) {
        return checkIfExist(id).map(found -> {
                    found.setFullName(dto.getFullName());
                    return found;
                }).flatMap(repository::save)
                .map(mapper::toDto);
    }

    @Override
    public Mono<Void> deleteCustomer(String id) {
        return checkIfExist(id).flatMap(customer -> repository.deleteById(id));
    }

    private Mono<Customer> checkIfExist(String id) {
        return repository.findById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)));
    }
}
