package com.programacionreactiva.reactive.fluxandmonotest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoInfiniteEjemplo {


    @Test
    public void infiniteStream() throws InterruptedException {
        Flux<Long> interval = Flux.interval(Duration.ofMillis(200)).log();

        interval.subscribe((elemento) -> System.out.println("elemento " + elemento));

        Thread.sleep(6000);


    }

    @Test
    public void infiniteStreamEjemplo2() throws InterruptedException {
        Flux<Integer> interval = Flux.interval(Duration.ofMillis(200))
                .map(nro -> nro.intValue())
                .take(3)
                .log();

        StepVerifier.create(interval)
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();

    }

    @Test
    public void infiniteStreamEjemplo2_withdelay() throws InterruptedException {
        Flux<Integer> interval = Flux.interval(Duration.ofMillis(200))
                .delayElements(Duration.ofSeconds(2))
                .map(nro -> nro.intValue())
                .take(3)
                .log();

        StepVerifier.create(interval)
                .expectSubscription()
                .expectNext(0,1,2)
                .verifyComplete();

    }


}
