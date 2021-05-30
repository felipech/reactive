package com.programacionreactiva.reactive.handler;


import com.programacionreactiva.reactive.constants.ItemConstants;
import com.programacionreactiva.reactive.document.Item;
import com.programacionreactiva.reactive.repository.ItemReactiveRepository;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
public class ItemHandlerTst {
    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public List<Item> data(){
        List<Item> itemList = Arrays.asList(new Item(null, "Samsung tv", 400L),
                new Item(null, "LG tv", 300L),
                new Item(null, "TCL tv", 340L),
                new Item(null, "Huawei phone", 2200L),
                new Item("QWERT", "Apple watch", 200L));
        return itemList;
    }


    @Before
    public void setUp(){
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("item inserted test " + item))
                .blockLast();
    }

    @Test
    public void getAllItems(){
        webTestClient.get().uri(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBodyList(Item.class)
                .hasSize(5);
    }

    @Test
    public void getAllItemsVersion2(){
        webTestClient.get().uri(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .expectBodyList(Item.class)
                .hasSize(5)
                .consumeWith(respuesta -> {
                    List<Item> responseBody = respuesta.getResponseBody();
                    responseBody.forEach(item -> {
                        TestCase.assertNotNull(item.getId());
                    } );
                });
    }

    @Test
    public void getAllItemsVersion3(){
        Flux<Item> itemFlux = webTestClient.get().uri(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
                .returnResult(Item.class)
                .getResponseBody();

        StepVerifier.create(itemFlux)
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();

    }

    @Test
    public void getOneItem(){
        webTestClient.get().uri(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1.concat("/{id}"),"QWERT")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 200L );
    }

    @Test
    public void getOneItem_notFound(){
        webTestClient.get().uri(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1.concat("/{id}"),"asd")
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    public void createItem(){
        Item item = new Item(null, "Huawei Mate 5", 877L);

        webTestClient.post().uri(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("Huawei Mate 5")
                .jsonPath("$.price").isEqualTo(877L);

    }


    @Test
    public void deleteItem(){
        webTestClient.delete().uri(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1.concat("/{id}"),"QWERT")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void updateItem(){
        Long precioNuevo = 100L;

        Item item = new Item(null, "Apple watch", precioNuevo);
        //siempre fijarce que al crear la uri este igual y que no falte ningun / o letra etc
        webTestClient.put().uri(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1.concat("/{id}"), "QWERT")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", precioNuevo);
    }

    @Test
    public void updateItem_notFound(){
        Long precioNuevo = 100L;

        Item item = new Item(null, "Apple watch", precioNuevo);
        //siempre fijarce que al crear la uri este igual y que no falte ningun / o letra etc
        webTestClient.put().uri(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1.concat("/{id}"), "aaa")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    public void runTimeExceptionTst(){
        webTestClient.get().uri("/fun/runtimeexception")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("Runtime exception");
    }


}
