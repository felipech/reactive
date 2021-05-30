package com.programacionreactiva.reactive.router;

import com.programacionreactiva.reactive.constants.ItemConstants;
import com.programacionreactiva.reactive.handler.ItemHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.programacionreactiva.reactive.constants.ItemConstants.ITEM_STREAM_FUNC_END_POINT_V1;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ItemsRouters {

    @Bean
    public RouterFunction<ServerResponse> itemRoute(ItemHandler itemsHandler){
        return RouterFunctions
                .route(GET(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1).and(accept(MediaType.APPLICATION_JSON))
                ,itemsHandler::getAllItems)
                .andRoute(GET(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1+"/{id}").and(accept(MediaType.APPLICATION_JSON))
                ,itemsHandler::getOneItem)
                .andRoute(POST(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1).and(accept(MediaType.APPLICATION_JSON))
                ,itemsHandler::createItem)
                .andRoute(DELETE(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1+"/{id}").and(accept(MediaType.APPLICATION_JSON))
                ,itemsHandler::deleteItem)
                .andRoute(PUT(ItemConstants.ITEM_FUNCTIONAL_END_POINT_V1+"/{id}").and(accept(MediaType.APPLICATION_JSON))
                ,itemsHandler::updateItem);
    }

    @Bean
    public RouterFunction<ServerResponse> errorRoute(ItemHandler itemHandler){
        return RouterFunctions
                .route(GET("/fun/runtimeexception").and(accept(MediaType.APPLICATION_JSON))
                ,itemHandler::itemEx);
    }

    @Bean
    public RouterFunction<ServerResponse> streamRouter(ItemHandler itemHandler){

        return RouterFunctions
                .route(GET(ITEM_STREAM_FUNC_END_POINT_V1).and(accept(MediaType.TEXT_EVENT_STREAM))
                ,itemHandler::itemStreamHndlr);
    }

}
