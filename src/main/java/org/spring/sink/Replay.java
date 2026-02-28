package org.spring.sink;

import org.spring.common.Util;
import reactor.core.publisher.Sinks;

public class Replay {

    static void main(String[] args) {
        // late subscriber can see all the messages
        demo1();

    }


    private static void demo1(){

        // handle through which we would push items
        var sink = Sinks.many().replay().all(1);

        // handle through which subscribers will receive items
        var flux = sink.asFlux();

        flux.subscribe(Util.subscriber("sam"));
        flux.subscribe(Util.subscriber("mike"));

        sink.tryEmitNext("hi");
        sink.tryEmitNext("how are you");
        sink.tryEmitNext("?");

        Util.sleepSeconds(2);
        //late subscriber can see all the messages
        flux.subscribe(Util.subscriber("jake"));
        sink.tryEmitNext("new message");

    }

}