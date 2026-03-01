package org.spring.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

/*
    Context is an immutable map. We can append additional info!
 */
public class ContextAppendUpdate {

    private static final Logger log = LoggerFactory.getLogger(ContextAppendUpdate.class);

    static void main(String[] args) {
        //append();
        update();
    }

    private static void append(){
        getWelcomeMessage()
                .contextWrite(Context.of("user", "sam").put("c", "d").put("e", "f"))
                .contextWrite(Context.of("a", "b"))
                .subscribe(Util.subscriber());
    }

    private static void update(){
        getWelcomeMessage()
                //update existing context
                .contextWrite(ctx -> ctx.put("user", ctx.get("user").toString().toUpperCase()))
                //deleted from context
                .contextWrite(ctx -> ctx.delete("c"))
                //append to context
                .contextWrite(Context.of("a", "b").put("c", "d").put("e", "f"))
                .contextWrite(Context.of("user", "sam"))//start adding context info
                .subscribe(Util.subscriber());
    }

    private static Mono<String> getWelcomeMessage(){
        return Mono.deferContextual(ctx -> {
            log.info("{}", ctx);
            if(ctx.hasKey("user")){
                return Mono.just("Welcome %s".formatted(ctx.get("user").toString()));
            }
            return Mono.error(new RuntimeException("unauthenticated"));
        });
    }


}