package com.example.mongo.service;

import com.example.mongo.dto.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerDTO> getAll();
    Mono<CustomerDTO> getById(String id);
    Mono<CustomerDTO> addCustomer(Mono<CustomerDTO> dto);
    Mono<CustomerDTO> editCustomer(String id, CustomerDTO dto);
    Mono<Void> deleteCustomer(String id);
}
