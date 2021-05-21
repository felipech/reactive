package com.programacionreactiva.reactive.repositorytests;

import com.programacionreactiva.reactive.document.Item;
import com.programacionreactiva.reactive.repository.ItemReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

@DataMongoTest
@RunWith(SpringRunner.class)
@DirtiesContext
public class ItemRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    List<Item> itemList = Arrays.asList(new Item(null, "Samsung tv", 400L),
            new Item(null, "LG tv", 300L),
            new Item(null, "TCL tv", 340L),
            new Item(null, "Huawei phone", 2200L),
            new Item("QWERT", "Apple watch", 200L));
    @Before
    public void setUp(){
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("Inserted: " + item))
                .blockLast();

    }

    @Test
    public void getAllItems(){
        StepVerifier.create(itemReactiveRepository.findAll())
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getOneItems(){
        StepVerifier.create(itemReactiveRepository.findById("QWERT"))
                .expectSubscription()
                .expectNextMatches(item -> item.getDescription().equals("Apple watch"))
                .verifyComplete();
    }

    @Test
    public void findItemByDescription(){
        StepVerifier.create(itemReactiveRepository.findByDescription("Apple watch").log())
                .expectSubscription()
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void saveItem(){
        Item item = new Item("TES","Motorola one", 140L);
        Mono<Item> itemMono = itemReactiveRepository.save(item);

        StepVerifier.create(itemMono.log("grabando: "))
                .expectSubscription()
                .expectNextMatches(item1 -> (item.getId() != null && item.getDescription().equals("Motorola one")))
                .verifyComplete();
    }

    @Test
    public void updateItem(){
        long priceUpdate = 230L;
        Flux<Item> tstUpdate = itemReactiveRepository.findByDescription("TCL tv")
                .map(item -> {
                    item.setPrice(priceUpdate);
                    return item;
                })
                .flatMap((item) -> {
                    return itemReactiveRepository.save(item);
                });

        StepVerifier.create(tstUpdate)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == priceUpdate)
                .verifyComplete();
    }

    @Test
    public void deleteItem(){

        Mono<Void> qwert = itemReactiveRepository.findById("QWERT")
                .map(Item::getId)
                .flatMap((id) -> itemReactiveRepository.deleteById(id));

        StepVerifier.create(qwert)
                .expectSubscription()
                .verifyComplete();
    }

}
