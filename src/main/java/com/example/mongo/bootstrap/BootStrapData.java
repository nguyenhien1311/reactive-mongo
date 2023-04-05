package com.example.mongo.bootstrap;

import com.example.mongo.domain.Beer;
import com.example.mongo.domain.Customer;
import com.example.mongo.repository.BeerRepository;
import com.example.mongo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootStrapData implements CommandLineRunner {
    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        customerRepository.deleteAll().doOnSuccess(success -> {
            loadCustomerData();
        }).subscribe();

        beerRepository.deleteAll().doOnSuccess(success -> {
            loadBeerData();
        }).subscribe();
    }

    private void loadBeerData() {
        Beer volcano = Beer.builder()
                .beerName("Volcano Tear")
                .quantityOnHand(1)
                .upc("Mystic")
                .beerStyle("Fantasy")
                .price(BigDecimal.TEN)
                .build();
        Beer moon = Beer.builder()
                .beerName("Moon dust")
                .beerStyle("Fantasy")
                .upc("Mystic")
                .price(BigDecimal.TEN)
                .quantityOnHand(20).build();
        Beer apple = Beer.builder()
                .beerName("Golden Apple")
                .quantityOnHand(100)
                .upc("Rare")
                .beerStyle("Holy")
                .price(BigDecimal.TEN)
                .build();
        Beer scale = Beer.builder()
                .beerName("Drake Scale")
                .beerStyle("Material")
                .upc("Mystic")
                .price(BigDecimal.TEN)
                .quantityOnHand(99).build();
        beerRepository.saveAll(List.of(volcano, moon, apple, scale)).subscribe();
    }

    private void loadCustomerData() {
        Customer peter = Customer.builder().fullName("Peter Packer").build();
        Customer tony = Customer.builder().fullName("Tony Stark").build();
        Customer lalo = Customer.builder().fullName("Lalo Salamanca").build();
        Customer heisenberg = Customer.builder().fullName("Walter White").build();

        customerRepository.saveAll(List.of(heisenberg, lalo, peter, tony)).subscribe();
    }
}
