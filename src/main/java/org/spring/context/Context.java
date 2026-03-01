package org.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Mono;

/*
    Context is for providing metadata about the request (similar to HTTP headers)
    context is simply a map
 */
public class Context {

    private static final Logger log = LoggerFactory.getLogger(Context.class);

    static void main(String[] args) {

        getWelcomeMessage()
                // below info can be sent using contextWrite without having to modify method signature
                .contextWrite(reactor.util.context.Context.of("user", "sam"))//used to send context
                .subscribe(Util.subscriber());

    }

    private static Mono<String> getWelcomeMessage(){
        return Mono.deferContextual(ctx -> {
            if(ctx.hasKey("user")){// will throw error if key does not match
                return Mono.just("Welcome %s".formatted(ctx.get("user").toString()));
            }
            return Mono.error(new RuntimeException("unauthenticated"));
        });
    }

}