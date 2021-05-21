package com.programacionreactiva.reactive.controllerflux;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@WebFluxTest
@RunWith(SpringRunner.class)
public class ControllerT1Ejemplo {

    //para la version no blocking yt para la que es bloqueante se puede usar resttemplatetest
    @Autowired
    WebTestClient webTestClient;

    @Test
    public void fluxAproach(){
        Flux<Integer> responseBody = webTestClient.get().uri("/flux")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .exchange()
                .expectStatus().isOk()
                .returnResult(Integer.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectSubscription()
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .verifyComplete();

    }


    @Test
    public void fluaxAproach2(){
        webTestClient.get().uri("/flux")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE) +";" + "charset=UTF-8")
                .expectBodyList(Integer.class)
                .hasSize(4);
    }

    @Test
    public void fluaxAproach3(){

        List<Integer> expectedList = Arrays.asList(1,2,3,4);

        EntityExchangeResult<List<Integer>> listEntityExchangeResult = webTestClient.get().uri("/flux")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .returnResult();

        assertEquals(expectedList, listEntityExchangeResult.getResponseBody());

    }

    @Test
    public void fluaxAproach4(){

        List<Integer> expectedList = Arrays.asList(1,2,3,4);

        webTestClient.get().uri("/flux")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Integer.class)
                .consumeWith((response) -> assertEquals(expectedList, response.getResponseBody()));

    }

    @Test
    public void fluxStream1(){
        Flux<Long> responseBody = webTestClient.get().uri("/fluxstream")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .exchange()
                .expectStatus().isOk()
                .returnResult(Long.class)
                .getResponseBody();

        StepVerifier.create(responseBody)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .thenCancel()
                .verify();
    }


    @Test
    public void monoCase1(){
        Integer expected = 1;

        webTestClient.get().uri("/mono")
                .accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .consumeWith((response) -> {
                    assertEquals(expected, response.getResponseBody());
                });


    }



}
