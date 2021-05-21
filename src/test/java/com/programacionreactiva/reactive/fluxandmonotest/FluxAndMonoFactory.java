package com.programacionreactiva.reactive.fluxandmonotest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactory {

    List<String> ejemploLista = Arrays.asList("nombre1", "nombre2", "nombre3");

    @Test
    public void fluxUsandoIterable(){
        Flux<String> flux = Flux.fromIterable(ejemploLista)
                .log();

        StepVerifier.create(flux)
                .expectNext("nombre1", "nombre2", "nombre3")
                .verifyComplete();

    }

    @Test
    public void fluxUsandoArray(){
        String [] nombres = new String[]{"nombre1", "nombre2", "nombre3"};

        Flux<String> nombreArrays = Flux.fromArray(nombres)
                .log();

        StepVerifier.create(nombreArrays)
                .expectNext("nombre1", "nombre2", "nombre3")
                .verifyComplete();

    }

    @Test
    public void fluxUsandoStream(){

        Flux<String> nombreStream = Flux.fromStream(ejemploLista.stream())
                .log();

        StepVerifier.create(nombreStream)
                .expectNext("nombre1", "nombre2", "nombre3")
                .verifyComplete();

    }

    @Test
    public void monoUsandoJustoEmpty(){

        Mono<Object> objectMono = Mono.justOrEmpty(null).log();

        StepVerifier.create(objectMono)
                .verifyComplete();

    }
    @Test
    public void monoUsandoSuplier(){
        Supplier<String> stringSupplier = () -> "felipe";

        Mono<String> objectMono = Mono.fromSupplier(stringSupplier).log();

        StepVerifier.create(objectMono)
                .expectNext("felipe")
                .verifyComplete();

    }

    @Test
    public void fluxUsandoRange(){
        Flux<Integer> range = Flux.range(1, 5).log();

        StepVerifier.create(range)
                .expectNext(1,2,3,4,5)
                .verifyComplete();

    }

}
