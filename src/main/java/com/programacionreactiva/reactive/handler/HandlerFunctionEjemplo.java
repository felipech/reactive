package com.programacionreactiva.reactive.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class HandlerFunctionEjemplo {

    public Mono<ServerResponse> flux(ServerRequest serverRequest){
        return ServerResponse.ok()
                .contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .body(
                        Flux.just(1,2,3,4,5)
                        .log(), Integer.class
                );
    }
}
