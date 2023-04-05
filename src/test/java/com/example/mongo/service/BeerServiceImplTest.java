package com.example.mongo.service;

import com.example.mongo.domain.Beer;
import com.example.mongo.dto.BeerDTO;
import com.example.mongo.mappers.BeerMapper;
import com.example.mongo.repository.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;

@SpringBootTest
public class BeerServiceImplTest {
    @Autowired
    BeerService service;
    @Autowired
    BeerRepository repository;
    @Autowired
    BeerMapper mapper;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = mapper.toDto(getBeer());

//        repository.count().subscribe(aLong -> {
//            if (aLong == 0) repository.save(getBeer());
//        });
    }

    @Test
    void addBeer() throws InterruptedException {
        AtomicReference<BeerDTO> atomic = new AtomicReference<>();
        beerDTO.setBeerName("Lolalitalelo");
        Mono<BeerDTO> dtoMono = service.addBeer(Mono.just(beerDTO));
        dtoMono.subscribe(dto -> {
            System.out.println(dto);
            atomic.set(dto);
        });
        await().until(() -> atomic.get() != null);
    }

    @Test
    void getById() {
        AtomicReference<BeerDTO> atomic = new AtomicReference<>();
        BeerDTO blockFirst = service.getAll().blockFirst();
        assert blockFirst != null;
        service.getById(blockFirst.getId()).subscribe(found -> {
            System.out.println(found.toString());
            atomic.set(found);
        });

        await().until(() -> atomic.get() != null);
    }

    @Test
    void getByName() {
        Beer beer = getBeer();
        AtomicBoolean atomic = new AtomicBoolean(false);
        Mono<BeerDTO> dtoMono = service.getByName("Golden Apple");

        dtoMono.subscribe(dto -> {
            System.out.println(dto.toString());
            atomic.set(true);
        });

        await().untilTrue(atomic);
    }

    public static Beer getBeer() {
        return Beer.builder()
                .beerName("Kraken Skin")
                .beerStyle("Fantasy")
                .upc("Mystic")
                .price(BigDecimal.TEN)
                .quantityOnHand(10)
                .build();
    }

    @Test
    void getAll() {
        AtomicBoolean atomic = new AtomicBoolean(false);
        service.getAll().subscribe(dto -> {
            atomic.set(true);
            System.out.println(dto.toString());
        });

        await().untilTrue(atomic);
    }

    @Test
    void getAllByStyle() {
        AtomicBoolean atomic = new AtomicBoolean(false);
        service.getAllByBeerStyle("Fantasy").subscribe(dto -> {
            atomic.set(true);
            System.out.println(dto.toString());
        });

        await().untilTrue(atomic);
    }

    @Test
    void editBeer() {
        AtomicReference<BeerDTO> atomic = new AtomicReference<>();
        beerDTO.setBeerName("Edited limited beer ediiit");
        BeerDTO first = service.getAll().blockFirst();
        assert first != null;
        service.editBeer(first.getId(), beerDTO).subscribe(edited -> {
            atomic.set(edited);
            System.out.println(edited.toString());
        });

        await().until(() -> atomic.get() != null);
    }

    @Test
    void deleteBeer() {
        AtomicBoolean atomic = new AtomicBoolean(false);

        BeerDTO last = service.getAll().blockLast();
        assert last != null;
        service.deleteBeer(last.getId()).subscribe();
        service.getById(last.getId()).subscribe(dto -> {
            atomic.set(true);
        });
        await().untilTrue(atomic);
    }
}