package org.spring.threadingAndSchedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import org.spring.hotColdPublishers.HotPublisher;
import reactor.core.publisher.Flux;

public class PubSubDefault {
    private static final Logger log = LoggerFactory.getLogger(PubSubDefault.class);
    // By default whichever thread subscribe will to the entire execution
    static void main() {
        var flux = Flux.create(sink -> {
            for (int i = 1; i < 3; i++) {
                log.info("Generating: {}", i);
                sink.next(i);
            }
            sink.complete();
        }).doOnNext(value -> log.info("value: {}", value));

        Runnable runnable =  () -> flux.subscribe(Util.subscriber("sub1"));
        Thread.ofPlatform().start(runnable);
    }
}
