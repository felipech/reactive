package com.programacionreactiva.reactive.fluxandmonotest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluaxMapTest {


    List<String> ejemploLista = Arrays.asList("nombre1", "nombre2", "nombre3", "alfred");


    @Test
    public void transformFluxUsingMap(){

        Flux<String> stringFlux = Flux.fromIterable(ejemploLista)
                .map(String::toUpperCase)
                .log();

        StepVerifier.create(stringFlux).expectNext("NOMBRE1", "NOMBRE2", "NOMBRE3", "ALFRED").verifyComplete();
    }
    @Test
    public void transformFluxUsingFlatMap(){

        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("A","B","C","D"))
                .flatMap(s -> {
                    return Flux.fromIterable(convertToList(s));
                })//puede ser la llamada a una db por ejemplo por cada elemento pasado en el flux
                .log();


        StepVerifier.create(stringFlux).expectNextCount(8).verifyComplete();
    }

    private List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s, "valor nuevo");

    }

}
