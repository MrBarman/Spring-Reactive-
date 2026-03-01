package org.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.context.Context;

public class ContextPropagation {

    private static final Logger log = LoggerFactory.getLogger(ContextPropagation.class);

    static void main(String[] args) {

        getWelcomeMessage()
                .concatWith(Flux.merge(producer1(),
                        //emptying producer
                        producer2().contextWrite(ctx -> Context.empty())))
                .contextWrite(Context.of("user", "sam"))
                .subscribe(Util.subscriber());

        Util.sleepSeconds(2);

    }

    private static Mono<String> getWelcomeMessage() {
        return Mono.deferContextual(ctx -> {
            if (ctx.hasKey("user")) {
                return Mono.just("Welcome %s".formatted(ctx.get("user").toString()));
            }
            return Mono.error(new RuntimeException("unauthenticated"));
        });
    }

    private static Mono<String> producer1() {
        return Mono.<String>deferContextual(ctx -> {
                       log.info("producer1: {}", ctx);
                       return Mono.empty();
                   })
                   .subscribeOn(Schedulers.boundedElastic());
    }

    private static Mono<String> producer2() {
        return Mono.<String>deferContextual(ctx -> {
                       log.info("producer2: {}", ctx);
                       return Mono.empty();
                   })
                   .subscribeOn(Schedulers.parallel());
    }


}