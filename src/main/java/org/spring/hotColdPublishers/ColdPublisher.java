package org.spring.hotColdPublishers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicInteger;

public class ColdPublisher {
    private static final Logger log = LoggerFactory.getLogger(ColdPublisher.class);
    /**
     * in case of cold publisher each subscriber will have its own data e.g. netflix subscription
     * use cases involve DB calls, API calls, File reads
     * Each request should execute independently
     (typical in Spring Boot REST APIs)
     */

    static void main() {
      //  AtomicInteger counter = new AtomicInteger();
        var flux = Flux.create(sink -> {
            log.info("Invoking cold publisher");
            for (int i = 0; i < 3; i++) {
                sink.next(i);
            }
        });
        flux.subscribe(Util.subscriber("sub1"));
        flux.subscribe(Util.subscriber("sub2"));
    }
}
