package com.programacionreactiva.reactive.handler;


import com.programacionreactiva.reactive.document.Item;
import com.programacionreactiva.reactive.document.ItemCapped;
import com.programacionreactiva.reactive.repository.ItemReactiveCappedRepository;
import com.programacionreactiva.reactive.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
public class ItemHandler {

    final ItemReactiveRepository itemReactiveRepository;

    final ItemReactiveCappedRepository itemReactiveCappedRepository;

    static Mono<ServerResponse> notFound = ServerResponse.notFound().build();

    @Autowired
    public ItemHandler(ItemReactiveRepository itemReactiveRepository, ItemReactiveCappedRepository itemReactiveCappedRepository) {
        this.itemReactiveRepository = itemReactiveRepository;
        this.itemReactiveCappedRepository = itemReactiveCappedRepository;
    }

    public Mono<ServerResponse> getAllItems(ServerRequest serverRequest){
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemReactiveRepository.findAll(), Item.class);
    }

    public Mono<ServerResponse> getOneItem(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Item> itemMono = itemReactiveRepository.findById(id);
        return itemMono.flatMap(item ->
                ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromValue(item))
                        .switchIfEmpty(notFound));
    }

    public Mono<ServerResponse> createItem(ServerRequest serverRequest){
        Mono<Item> itemMono = serverRequest.bodyToMono(Item.class);

        return itemMono.flatMap(item ->
                ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(itemReactiveRepository.save(item),Item.class));

    }

    public Mono<ServerResponse> deleteItem(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        Mono<Void> deleteItem = itemReactiveRepository.deleteById(id);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(deleteItem,Void.class);

    }

    public Mono<ServerResponse> updateItem(ServerRequest serverRequest){
        String id = serverRequest.pathVariable("id");
        Mono<Item> itemFinal = serverRequest.bodyToMono(Item.class)
                .flatMap(item -> itemReactiveRepository.findById(id)
                        .flatMap(itemActualSinCambios -> {
                            itemActualSinCambios.setPrice(item.getPrice());
                            itemActualSinCambios.setDescription(item.getDescription());
                            return itemReactiveRepository.save(itemActualSinCambios);
                        }));

        return itemFinal.flatMap(item -> ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(item))
                ).switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> itemEx(ServerRequest serverRequest) {
        throw new RuntimeException("Runtime exception");
    }

    public Mono<ServerResponse> itemStreamHndlr(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(itemReactiveCappedRepository.findItemCappedBy(), ItemCapped.class);

    }
}
