package com.programacionreactiva.reactive.router;

import com.programacionreactiva.reactive.handler.HandlerFunctionEjemplo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class RouterFunctionEjemplo {

    @Bean
    public RouterFunction<ServerResponse> route(HandlerFunctionEjemplo handlerFunctionEjemplo){
        return RouterFunctions
                .route(GET("/functionalEndPoint/flux").and(accept(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE))), handlerFunctionEjemplo::flux);
    }

}
