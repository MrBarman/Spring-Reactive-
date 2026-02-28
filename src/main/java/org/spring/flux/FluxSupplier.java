package org.spring.flux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;

import java.util.List;

public class FluxSupplier {
    private static final Logger log = LoggerFactory.getLogger(FluxSupplier.class);

    public static void main(String[] args) {
        //create flux using just
        log.info("creating flux from just");
        Flux.just(1,2,3,"test")
                .subscribe(Util.subscriber());

        //multiple subscriber example
        log.info("creating multiple subscriber");
        var flux = Flux.just(1,2,3);

        flux.subscribe(Util.subscriber("sub1"));
        flux.subscribe(Util.subscriber("sub22"));
        flux.filter(i->i%2 ==0)
                .subscribe(Util.subscriber("sub3"));

        //flux from iterable
        log.info("creating flux from iterable");
        var fluxList = List.of("a","b","c");
        Flux.fromIterable(fluxList)
                .subscribe(Util.subscriber());

        //flux from array
        log.info("creating flux from array");
        Integer[] intArr = {1,2,3,4,5,6,7,8};
        Flux.fromArray(intArr)
                .subscribe(Util.subscriber());

        log.info("creating flux in range, first param is starting an 2nd is total from start");
        Flux.range(5,8)
                .subscribe(Util.subscriber());


    }
}
