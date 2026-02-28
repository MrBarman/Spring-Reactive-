package org.spring.sink;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class SinkThreadSafety {

    private static final Logger log = LoggerFactory.getLogger(SinkThreadSafety.class);

    static void main(String[] args) {

        demo2();

    }

    private static void demo1(){
        var sink = Sinks.many().unicast().onBackpressureBuffer();
        var flux = sink.asFlux();

        // arraylist is not thread safe.
        // intentionally chosen for demo purposes.
        var list = new ArrayList<>();
        flux.subscribe(list::add);

        for (int i = 0; i < 1000; i++) {
            var j = i;
            CompletableFuture.runAsync(() -> {
                //does not guarantee delivery, different res always
                // in this case list size will not 1000 as expected, always will be different value
                sink.tryEmitNext(j);
            });
        }

        Util.sleepSeconds(2);

        log.info("list size: {}", list.size());
    }

    private static void demo2(){
        var sink = Sinks.many().unicast().onBackpressureBuffer();
        var flux = sink.asFlux();

        // arraylist is not thread safe.
        // intentionally chosen for demo purposes.
        var list = new ArrayList<>();
        flux.subscribe(list::add);

        for (int i = 0; i < 1000; i++) {
            var j = i;
            CompletableFuture.runAsync(() -> {
                sink.emitNext(j, (signal, emitResult) -> {
                    // guarantees delivery but require to handle failure using Sinks.EmitResult
                    // list size will be 1000 for sure
                    return Sinks.EmitResult.FAIL_NON_SERIALIZED.equals(emitResult);
                });
            });
        }

        Util.sleepSeconds(2);

        log.info("list size: {}", list.size());
    }


}