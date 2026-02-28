package org.spring.flux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;

import java.util.List;

public class FluxFromStream {
    private static final Logger log = LoggerFactory.getLogger(FluxFromStream.class);

    public static void main(String[] args) {
        var list = List.of(1,2,3,4,5);
        var streamList = list.stream();
        log.info("flux from stream will fail");
        Flux.fromStream(streamList)
                .subscribe(Util.subscriber("sub1"));
        //below will not work as stream will be already finished
       // java stream is one time use. once consumed it cannot be reused
        Flux.fromStream(streamList)
                .subscribe(Util.subscriber("sub2"));

        log.info("flux from stream will work as supplier is provided at the time of creation");
        //to solve able we need to provide the supplier at the time of creation of stream like below
        Flux.fromStream(()->list.stream())
                .subscribe(Util.subscriber("sub1"));
        //below will not work as stream will be already finished
        //java stream is one time use. once consumed it cannot be reused
        Flux.fromStream(()->list.stream())
                .subscribe(Util.subscriber("sub2"));

    }
}
