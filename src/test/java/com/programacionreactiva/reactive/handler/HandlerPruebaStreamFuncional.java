package com.programacionreactiva.reactive.handler;


import com.programacionreactiva.reactive.constants.ItemConstants;
import com.programacionreactiva.reactive.document.ItemCapped;
import com.programacionreactiva.reactive.repository.ItemReactiveCappedRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureWebTestClient
@DirtiesContext
public class HandlerPruebaStreamFuncional {

    @Autowired
    ItemReactiveCappedRepository itemReactiveCappedRepository;

    @Autowired
    ReactiveMongoOperations reactiveMongoOperations;

    @Autowired
    WebTestClient webTestClient;

    @Before
    public void inicializrData(){
        //en las ultimas versiones de spring es necesario subscribirse a la operacion explicitamente no como en versiones anteriores donde subscriber lo contenia el metodo
        //createCollection
        reactiveMongoOperations.dropCollection(ItemCapped.class)
                .then(reactiveMongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped())).subscribe();

        Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofMillis(100))
                .map(i -> new ItemCapped(null,"Random Item " + i, (100+i))).take(5);

        //blocklast contiene subscriber por eso no es necesario subscribirce explicitamente
        itemReactiveCappedRepository.insert(itemCappedFlux)
                .doOnNext((itemCapped -> System.out.println("Inserted Item is " + itemCapped))).blockLast();

    }

    @Test
    public void tstStreamAllItems(){
        Flux<ItemCapped> testCappedCollection = webTestClient.get().uri(ItemConstants.ITEM_STREAM_FUNC_END_POINT_V1).exchange()
                .expectStatus().isOk()
                .returnResult(ItemCapped.class)
                .getResponseBody()
                .take(5);


        StepVerifier.create(testCappedCollection)
                .expectNextCount(5)
                .thenCancel()
                .verify();
    }
}
