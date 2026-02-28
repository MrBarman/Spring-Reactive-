package org.spring.threadingAndSchedulers;

import org.spring.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

public class Test {
    static void main(String[] args) {
              //  test1();
                test2();
        Util.sleepSeconds(3);
    }

    private static void test1(){
        Flux.create(fluxSink -> {
                    for (int i = 0; i < 5; i++) {
                        fluxSink.next(i);
                    }
                    fluxSink.complete();
                })
                .map(i -> i + "a")
                .publishOn(Schedulers.boundedElastic())
                .subscribeOn(Schedulers.parallel())
                .subscribe(Util.subscriber());

    }

    private static void test2(){
        Flux<Object> flux = Flux.create(fluxSink -> {
                    for (int i = 0; i < 5; i++) {
                        fluxSink.next(i);
                    }
                    fluxSink.complete();
                })
                .subscribeOn(Schedulers.boundedElastic());

        flux.subscribeOn(Schedulers.parallel())
                .map(i -> i + "a")
                .subscribe(Util.subscriber());
    }
}
