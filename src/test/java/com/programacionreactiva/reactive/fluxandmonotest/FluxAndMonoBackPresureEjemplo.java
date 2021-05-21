package com.programacionreactiva.reactive.fluxandmonotest;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPresureEjemplo {

    @Test
    public void backPresureTest(){

        Flux<Integer> ejemp1 = Flux.range(1, 10)
                .log();
        StepVerifier.create(ejemp1)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenRequest(1)
                .expectNext(3)
                .thenCancel().verify();

    }

    @Test
    public void backPresureTest2(){

        Flux<Integer> ejemp2 = Flux.range(1, 10)
                .log();

        ejemp2.subscribe((elem) -> System.out.println("elementos  " + elem),
                (e) -> System.err.println("error "),
                () -> System.out.println("done"),
                (subscription -> subscription.request(2)));


    }

    @Test
    public void backPresureTest2Custom(){

        Flux<Integer> ejemp2 = Flux.range(1, 10)
                .log();

        ejemp2.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println("value " + value);
                if(value == 4){
                    cancel();
                }
            }
        });

    }

}
