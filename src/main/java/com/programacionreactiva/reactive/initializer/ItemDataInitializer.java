package com.programacionreactiva.reactive.initializer;

import com.mongodb.BasicDBObject;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.programacionreactiva.reactive.document.Item;
import com.programacionreactiva.reactive.document.ItemCapped;
import com.programacionreactiva.reactive.repository.ItemReactiveCappedRepository;
import com.programacionreactiva.reactive.repository.ItemReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class ItemDataInitializer implements CommandLineRunner {

    final
    ItemReactiveRepository itemReactiveRepository;

    final
    ItemReactiveCappedRepository itemReactiveCappedRepository;


    @Autowired
    ReactiveMongoOperations mongoOperations;

    public ItemDataInitializer(ItemReactiveRepository itemReactiveRepository, ItemReactiveCappedRepository itemReactiveCappedRepository) {
        this.itemReactiveRepository = itemReactiveRepository;
        this.itemReactiveCappedRepository = itemReactiveCappedRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        initialDataSetup();
        createdCappedCollections();
        initialDataSetupForCappedCollections();
    }

    private void createdCappedCollections() {


        mongoOperations.dropCollection(ItemCapped.class)
                .then(mongoOperations.createCollection(ItemCapped.class, CollectionOptions.empty().maxDocuments(20).size(50000).capped())).subscribe();
    }

    public List<Item> data(){
        return Arrays.asList(new Item(null, "Samsung tv", 400L),
                new Item(null, "LG tv", 300L),
                new Item(null, "TCL tv", 340L),
                new Item(null, "Huawei phone", 2200L),
                new Item("QWERTY", "Apple watch", 200L));
    }

    public void initialDataSetupForCappedCollections(){
        Flux<ItemCapped> itemCappedFlux = Flux.interval(Duration.ofSeconds(1))
                .map(i -> new ItemCapped(null,"Random Item " + i, (100+i)));

        itemReactiveCappedRepository.insert(itemCappedFlux)
                .subscribe((itemCapped -> log.info("Inserted Item is " + itemCapped)));
    }

    private void initialDataSetup() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe(item -> System.out.println("item inserted " + item));
    }



}
