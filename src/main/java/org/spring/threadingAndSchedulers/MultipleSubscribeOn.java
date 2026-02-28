package org.spring.threadingAndSchedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class MultipleSubscribeOn {
    private static final Logger log = LoggerFactory.getLogger(MultipleSubscribeOn.class);
    static void main(String[] args) {
        var flux = Flux.create(sink -> {
                    for (int i = 1; i < 3; i++) {
                        log.info("Generating: {}", i);
                        sink.next(i);
                    }
                    sink.complete();
                })
                //the closest one to the producer will take priority
                //here first bounded elastic will execute and
                // later all will be executed by custom subscribeon
                .subscribeOn(Schedulers.newParallel("custom subscribeOn"))
                .doOnNext(value -> log.info("value: {}", value))
                .doFirst(()->log.info("first1"))
                .subscribeOn(Schedulers.boundedElastic())
                .doFirst(()->log.info("first2"));

        Runnable runnable1 = () -> flux
                .subscribe(Util.subscriber());

        Runnable runnable2 = () -> flux
                .subscribe(Util.subscriber());

        Thread.ofPlatform().start(runnable1);
        Thread.ofPlatform().start(runnable2);

        Util.sleepSeconds(2);
    }
}
