package org.spring.combiningPublishers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/*
    "then" could be helpful when we are not interested in the result of a publisher /
    we need to have sequential execution of asynchronous tasks.
 */
public class ThenOperator {

    private static final Logger log = LoggerFactory.getLogger(ThenOperator.class);

    static void main(String[] args) {

        var records = List.of("a", "b", "c");

        saveRecords(records)
                .then(sendNotification(records)) // only in case of success
                .subscribe(Util.subscriber());


        Util.sleepSeconds(5);

    }

    private static Flux<String> saveRecords(List<String> records) {
        return Flux.fromIterable(records)
                   .map(r -> "saved " + r)
                   .delayElements(Duration.ofMillis(500));
    }

    private static Mono<Void> sendNotification(List<String> records) {
        return Mono.fromRunnable(() -> log.info("all these {} records saved successfully", records));
    }

}