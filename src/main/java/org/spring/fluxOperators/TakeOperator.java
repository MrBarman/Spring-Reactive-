package org.spring.fluxOperators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;

public class TakeOperator {

    public static final Logger log = LoggerFactory.getLogger(TakeOperator.class);

    public static void main(String[] args) {
        log.info("===== take() =====");
        takeOperator();
        log.info("===== takeWhile() =====");
        takeWhileOperator();
        log.info("===== takeUntil() =====");
        takeUntil();

        //test combined
        log.info("===== take combined =====");
        Flux.range(1, 100)
                .take(25)// emit till 25
                .takeWhile(i -> i <10) // emit till 9 from 25
                .takeUntil(i -> i > 1 && i < 5) // condition met at 1, 2 and it stops
                .take(5) // take 1 and 2 as only these 2 value emitted
                .subscribe(Util.subscriber());
        //takewhile is like limiting based on condition.
        // The stream terminates immediately after condition is met
//        Flux.range(1,10)
//             //   .log("take")
//               // .take(2)
//                .takeWhile(i->i<10)
//                .subscribe(Util.subscriber());

        // we can use take until also which stops when condition is met
    }

    private static void takeOperator() {
        // take works like limit
        Flux.range(1, 10)
                .take(4)
                .subscribe(Util.subscriber());

    }

    private static void takeWhileOperator() {
        //takeWhile is like limiting based on condition.
        // The stream terminates immediately after condition is met
        //f the condition is false then stop immediately
        Flux.range(1, 10)
                .takeWhile(i -> i < 9)
                .subscribe(Util.subscriber());

    }

    private static void takeUntil() {
        // we can use takeUntil which stops when condition is met
        //if the condition is true then stop immediately
        Flux.range(1, 10)
                .takeUntil(i -> i == 0)
                .subscribe(Util.subscriber());

    }
}
