package com.programacionreactiva.reactive.Controller.v1;

import com.programacionreactiva.reactive.constants.ItemConstants;
import com.programacionreactiva.reactive.document.ItemCapped;
import com.programacionreactiva.reactive.repository.ItemReactiveCappedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static com.programacionreactiva.reactive.constants.ItemConstants.ITEM_STREAM_END_POINT_V1;

@RestController
public class ItemStreamController {

    private final ItemReactiveCappedRepository itemReactiveCappedRepository;

    @Autowired
    public ItemStreamController(ItemReactiveCappedRepository itemReactiveCappedRepository) {
        this.itemReactiveCappedRepository = itemReactiveCappedRepository;
    }

    @GetMapping(value = ITEM_STREAM_END_POINT_V1, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ItemCapped> getStreamItemsCapped(){
        return itemReactiveCappedRepository.findItemCappedBy();
    }

}
