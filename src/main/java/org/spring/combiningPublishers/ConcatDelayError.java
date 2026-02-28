package org.spring.combiningPublishers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * concatDelayError used to delay the error if consumer is interested in data even if error occurs
 */
public class ConcatDelayError {

    private static final Logger log = LoggerFactory.getLogger(ConcatDelayError.class);

    public static void main(String[] args) {

        demo2();

        Util.sleepSeconds(3);

    }

    private static void demo1(){
        producer1()
                .concatWith(producer3())
                .concatWith(producer2())
                .subscribe(Util.subscriber());
    }

    private static void demo2(){// here producer3() error will be thrown in last even if order is in 2nd place
        Flux.concatDelayError(producer1(), producer3(), producer2())
                .subscribe(Util.subscriber());
    }

    private static Flux<Integer> producer1(){
        return Flux.just(1, 2, 3)
                   .doOnSubscribe(s -> log.info("subscribing to producer1"))
                   .delayElements(Duration.ofMillis(10));
    }

    private static Flux<Integer> producer2(){
        return Flux.just(51, 52, 53)
                   .doOnSubscribe(s -> log.info("subscribing to producer2"))
                   .delayElements(Duration.ofMillis(10));
    }

    private static Flux<Integer> producer3(){
        return Flux.error(new RuntimeException("oops"));
    }

}