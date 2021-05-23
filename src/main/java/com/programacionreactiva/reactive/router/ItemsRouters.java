package com.programacionreactiva.reactive.router;

import com.programacionreactiva.reactive.constants.ItemConstants;
import com.programacionreactiva.reactive.handler.ItemHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class ItemsRouters {

    @Bean
    public RouterFunction<ServerResponse> itemRoute(ItemHandler itemsHandler){
        return RouterFunctions
                .route(GET(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1).and(accept(MediaType.APPLICATION_JSON))
                ,itemsHandler::getAllItems);
    }

}
