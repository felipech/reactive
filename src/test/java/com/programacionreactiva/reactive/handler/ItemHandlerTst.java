package com.programacionreactiva.reactive.handler;


import com.programacionreactiva.reactive.constants.ItemConstants;
import com.programacionreactiva.reactive.document.Item;
import com.programacionreactiva.reactive.repository.ItemReactiveRepository;
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

}
