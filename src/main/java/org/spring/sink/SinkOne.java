package org.spring.sink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Sinks;

public class SinkOne {

    private static final Logger log = LoggerFactory.getLogger(SinkOne.class);

    static void main(String[] args) {

        demo2();

    }

    // exploring sink methods to emit item / empty / error
    private static void demo1(){
        var sink = Sinks.one();
        var mono = sink.asMono();
        //required to subscribe to get emitted data
        mono.subscribe(Util.subscriber());
        //sink.tryEmitValue("hi");
        //sink.tryEmitEmpty();
        sink.tryEmitError(new RuntimeException("oops"));
    }

    // we can have multiple subscribers
    private static void demo2(){
        var sink = Sinks.one();
        var mono = sink.asMono();
        sink.tryEmitValue("hi");
        mono.subscribe(Util.subscriber("sam"));
        mono.subscribe(Util.subscriber("mike"));
    }

    // emit failure handler - we can not emit after complete
    // sinkOne can emit once but can be subscribed by many
    private static void demo3(){
        var sink = Sinks.one();
        var mono = sink.asMono();

    //    mono.subscribe(Util.subscriber("sam"));
        mono.subscribe();

        sink.emitValue("hi", ((signalType, emitResult) -> {
            log.info("hi");
            log.info(signalType.name());
            log.info(emitResult.name());
            return false;
        }));

        sink.emitValue("hello", ((signalType, emitResult) -> {
            log.info("hello");
            log.info(signalType.name());
            log.info(emitResult.name());
            return false;
        }));

    }



}