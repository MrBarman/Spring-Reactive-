package org.spring.batching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * groupBy partitions items based on a key.
 * Any given time multiple flux can be open.
 */
public class GroupedFlux {

    private static final Logger log = LoggerFactory.getLogger(GroupedFlux.class);

    public static void main(String[] args) {

        Flux.range(1, 30)
            .delayElements(Duration.ofSeconds(1))
            .groupBy(i -> i % 2) // group by 0 or 1
            .flatMap(GroupedFlux::processEvents)
            .subscribe();

        Util.sleepSeconds(60);

    }

    private static Mono<Void> processEvents(reactor.core.publisher.GroupedFlux<Integer, Integer> groupedFlux) {
        log.info("received flux for {}", groupedFlux.key());
        return groupedFlux.doOnNext(i -> log.info("key: {}, item: {}", groupedFlux.key(), i))
                          .then();
    }

}