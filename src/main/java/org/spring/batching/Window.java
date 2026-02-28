package org.spring.batching;

import org.spring.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 *  window creates sub-streams for every n item or t time duration
 *  subscriber keep on changing for each window
 *  At a given window only one flux will open
 */
public class Window {

    static void main(String[] args) {

        eventStream()
             //   .window(Duration.ofMillis(1800))
                .window(5)
                .flatMap(Window::processEvents)
                .subscribe();


        Util.sleepSeconds(60);

    }

    private static Flux<String> eventStream() {
        return Flux.interval(Duration.ofMillis(500))
                   .map(i -> "event-" + (i + 1));
    }

    private static Mono<Void> processEvents(Flux<String> flux) {
        return flux.doOnNext(e -> System.out.print("*"))
                   .doOnComplete(System.out::println)
                   .then();
    }


}