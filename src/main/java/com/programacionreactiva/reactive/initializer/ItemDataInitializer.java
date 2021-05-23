package com.programacionreactiva.reactive.initializer;

import com.programacionreactiva.reactive.document.Item;
import com.programacionreactiva.reactive.repository.ItemReactiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Component
public class ItemDataInitializer implements CommandLineRunner {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    @Override
    public void run(String... args) throws Exception {
        initialDataSetup();
    }

    public List<Item> data(){
        return Arrays.asList(new Item(null, "Samsung tv", 400L),
                new Item(null, "LG tv", 300L),
                new Item(null, "TCL tv", 340L),
                new Item(null, "Huawei phone", 2200L),
                new Item("QWERTY", "Apple watch", 200L));
    }

    private void initialDataSetup() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .subscribe(item -> System.out.println("item inserted " + item));
    }


}
