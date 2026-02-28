package org.spring.mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MonoSupplier {
    private static final Logger log = LoggerFactory.getLogger(MonoSupplier.class);

    public static void main(String[] args) throws Exception {
        var list = List.of(1,2,3);
     //   Mono.just(sum(list))//it will execute even if not subscribed
       //         .subscribe();

        //exception required to be handled explicitly
         Mono.fromSupplier(()-> {
                     try {
                         log.info("executing supplier");
                         return sum(list);
                     } catch (Exception e) {
                         throw new RuntimeException(e);
                     }
                 }) //delay execution until list is supplied
                 .subscribe(Util.subscriber());

        //does not return anything
        Mono.fromRunnable(()-> {
            try {
                log.info("from runnable: {}",sum(list));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).subscribe(Util.subscriber());

         //no need to handle exception as it is part of method signature
        //blocking and synchronous
        Mono.fromCallable(()-> {
                    log.info("executing callable");
           return sum(list);
        }) //delay execution until list is supplied
                .subscribe(Util.subscriber());

        //mono from future use for async calls as it is nonblocking
        //returns completable future
        Mono.fromFuture(MonoSupplier::getName)
                .subscribe(Util.subscriber());
        Util.sleepSeconds(1);
    }


    private static int sum(List<Integer> list) throws Exception{
        log.info("finding the sum of {}",list);
        return list.stream().mapToInt(i->i).sum();
    }

    private static CompletableFuture<String> getName(){
        return CompletableFuture.supplyAsync(()-> {
                    log.info("Executing getName from completable future");
                   return Util.faker().name().firstName();
                });
    }

}
