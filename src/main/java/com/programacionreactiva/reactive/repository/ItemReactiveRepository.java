package com.programacionreactiva.reactive.repository;

import com.programacionreactiva.reactive.document.Item;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ItemReactiveRepository extends ReactiveMongoRepository<Item, String> {


    Flux<Item> findByDescription(String description);
}
