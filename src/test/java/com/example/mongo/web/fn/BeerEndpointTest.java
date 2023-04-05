package com.example.mongo.web.fn;

import com.example.mongo.dto.BeerDTO;
import com.example.mongo.service.BeerServiceImplTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

@SpringBootTest
@AutoConfigureWebTestClient
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BeerEndpointTest {
    @Autowired
    WebTestClient client;

    @Test
    void testListBeer() {
        client.get().uri(BeerRouterConfig.BEER_PATH)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isOk();
    }
    @Test
    void testListBeerWithStyle() {
        client.get().uri(BeerRouterConfig.BEER_PATH,"Holy")
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testBeerById() {
        BeerDTO dto = getTestSavedBeer();
        client.get().uri(BeerRouterConfig.BEER_PATH_WITH_ID, dto.getId())
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDTO.class);
    }
    @Test
    void testBeerByIdNotFound() {
        client.get().uri(BeerRouterConfig.BEER_PATH_WITH_ID, "As High As Honor")
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testAddBeer() {
        BeerDTO dto = getTestSavedBeer();
        client.post().uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(dto), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isCreated()
                .expectBody(BeerDTO.class);
    }

    @Test
    void testEditBeer() {
        BeerDTO dto = getTestSavedBeer();
        dto.setBeerName("Moon Flower");

        client.put().uri(BeerRouterConfig.BEER_PATH_WITH_ID, dto.getId())
                .body(Mono.just(dto), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }
    @Test
    void testEditNotFound() {
        BeerDTO dto = getTestSavedBeer();
        client.put().uri(BeerRouterConfig.BEER_PATH_WITH_ID, "We Light The Way")
                .body(Mono.just(dto), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testDeleteBeet() {
        BeerDTO dto = getTestSavedBeer();

        client.delete().uri(BeerRouterConfig.BEER_PATH_WITH_ID, dto.getId())
                .header("Content-type", "application/json")
                .exchange()
                .expectStatus().isNoContent();
    }

    public BeerDTO getTestSavedBeer() {
        FluxExchangeResult<BeerDTO> result = client.post().uri(BeerRouterConfig.BEER_PATH)
                .body(Mono.just(BeerServiceImplTest.getBeer()), BeerDTO.class)
                .header("Content-type", "application/json")
                .exchange()
                .returnResult(BeerDTO.class);

        return client.get().uri(BeerRouterConfig.BEER_PATH).exchange().returnResult(BeerDTO.class).getResponseBody().blockFirst();
    }
}
