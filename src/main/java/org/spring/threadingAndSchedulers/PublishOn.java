package org.spring.threadingAndSchedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
   PublishOn is very useful when working with specially I/O operation
    with some blocking processing task in between
 */
public class PublishOn {
    private static final Logger log = LoggerFactory.getLogger(PublishOn.class);
    static void main(String[] args) {
        var flux = Flux.create(sink -> {
                    for (int i = 1; i < 3; i++) {
                        log.info("Generating: {}", i);
                        sink.next(i);
                    }
                    sink.complete();
                })
                .publishOn(Schedulers.parallel())//first publish from another thread
                .doOnNext(value -> log.info("value: {}", value))
                .doFirst(()->log.info("first1"))
                .publishOn(Schedulers.boundedElastic())// 2nd publish from another thread
                .doFirst(()->log.info("first2"));

        Runnable runnable1 = () -> flux
                .subscribe(Util.subscriber("sub1"));

//        Runnable runnable2 = () -> flux
//                .subscribe(Util.subscriber());

        Thread.ofPlatform().start(runnable1);
//        Thread.ofPlatform().start(runnable2);

        Util.sleepSeconds(2);
    }
}
