package org.spring.repeatAndRetry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/*
    retry operator simply resubscribes when it sees error signal.
 */
public class Retry {

    private static final Logger log = LoggerFactory.getLogger(Retry.class);

    public static void main(String[] args) {

        demo2();

        Util.sleepSeconds(10);
    }

    private static void demo1() {
        getCountryName()
                .retry(4)
                .subscribe(Util.subscriber());
    }

    private static void demo2() {
        getCountryName()
                .retryWhen(
                        reactor.util.retry.Retry.fixedDelay(3, Duration.ofSeconds(1))
                             .filter(ex -> RuntimeException.class.equals(ex.getClass()))
                             .onRetryExhaustedThrow((spec, signal) -> signal.failure())
                )
                .subscribe(Util.subscriber());
    }

    private static Mono<String> getCountryName() {
        var atomicInteger = new AtomicInteger(0);
        return Mono.fromSupplier(() -> {
                       if (atomicInteger.incrementAndGet() < 5) {
                           throw new RuntimeException("oops");
                       }
                       return Util.faker().country().name();
                   })
                   .doOnError(err -> log.info("ERROR: {}", err.getMessage()))
                   .doOnSubscribe(s -> log.info("subscribing"))
                ;
    }

}