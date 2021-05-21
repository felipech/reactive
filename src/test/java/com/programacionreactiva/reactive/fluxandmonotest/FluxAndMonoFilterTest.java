package com.programacionreactiva.reactive.fluxandmonotest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {
    List<String> ejemploLista = Arrays.asList("nombre1", "nombre2", "nombre3", "alfred");
    @Test
    public void filterTest(){
        Flux<String> fromIterable = Flux.fromIterable(ejemploLista)
                .filter(s -> s.startsWith("a"))
                .log();
        StepVerifier.create(fromIterable).expectNext("alfred").verifyComplete();



    }
}
