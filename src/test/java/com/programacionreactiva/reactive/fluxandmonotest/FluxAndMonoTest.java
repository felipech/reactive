package com.programacionreactiva.reactive.fluxandmonotest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void fluxTest(){

        //flux de ejemplo al cual se le concatena un error para ver como se maneja
        Flux<String> flux = Flux.just("primero", "segundo", "tercero")
                .concatWith(Flux.error(new RuntimeException("error de ejemplo"))).log();

        //primer paso para usar flux es usar subscriber
        flux.subscribe(System.out::println, (error) -> System.out.println(error));


    }

    @Test
    public void fluxTestDosSinErrores(){
        Flux<String> flux = Flux.just("primero", "segundo", "tercero")
                .log();


        StepVerifier.create(flux)
            .expectNext("primero")
            .expectNext("segundo")
            .expectNext("tercero")
            .verifyComplete();

    }

    @Test
    public void fluxTestDosConErroresTipo(){
        Flux<String> flux = Flux.just("primero", "segundo", "tercero")
                .concatWith(Flux.error(new RuntimeException("ejemplo test error")))
                .log();


        StepVerifier.create(flux)
                .expectNext("primero")
                .expectNext("segundo")
                .expectNext("tercero")
                .expectError(RuntimeException.class)
                .verify();

    }

    @Test
    public void fluxTestDosConErroresMensaje(){
        Flux<String> flux = Flux.just("primero", "segundo", "tercero")
                .concatWith(Flux.error(new RuntimeException("ejemplo test error")))
                .log();


        StepVerifier.create(flux)
                .expectNext("primero")
                .expectNext("segundo")
                .expectNext("tercero")
                .expectErrorMessage("ejemplo test error")
                .verify();

    }

    @Test
    public void monoTest(){

        Mono<String> primero = Mono.just("primero");

        StepVerifier.create(primero)
                .expectNext("primero")
                .verifyComplete();

    }

    @Test
    public void monoTestError(){

        StepVerifier.create(Mono.error(new RuntimeException("error ejemplo")).log())
                .expectError()
                .verify();


    }

}
