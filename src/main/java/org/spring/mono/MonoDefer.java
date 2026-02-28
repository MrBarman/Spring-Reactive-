package org.spring.mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Mono;

import java.util.List;

public class MonoDefer {
    private static final Logger log = LoggerFactory.getLogger(MonoDefer.class);

    public static void main(String[] args) {
       Mono.defer(MonoDefer::createPublisher)//delay execution until list is supplied
                .subscribe(Util.subscriber());
    }


    private static Mono<Integer> createPublisher(){
        Util.sleepSeconds(1);
        log.info("creating publisher");
        var list = List.of(1,2,3);
       return Mono.fromSupplier(()->sum(list)); //delay execution until list is supplied

    }

    private static int sum(List<Integer> list){
        log.info("finding the sum of {}",list);
        Util.sleepSeconds(3);
        return list.stream().mapToInt(i->i).sum();
    }
}
