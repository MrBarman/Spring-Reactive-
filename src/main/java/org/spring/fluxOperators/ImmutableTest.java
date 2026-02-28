package org.spring.fluxOperators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

public class ImmutableTest {
    private static final Logger log = LoggerFactory.getLogger(ImmutableTest.class);

    public static void main(String[] args) {
        log.info("\n==== immutable ======");
        Flux<Integer> range = Flux.range(1, 10);
        range.map(i -> i * 10);
        range.subscribe(i->System.out.print(" "+i));
       System.out.println();
        log.info("\n==== mutable i.e assign to another flux  ======");
        Flux<Integer> range1 = Flux.range(1, 10);
        Flux<Integer> range2 = range.map(i -> i * 10);
        range2.subscribe(i->System.out.print(" "+i));
    }
}
