package org.spring.fluxOperators;

import reactor.core.publisher.Flux;

public class NextTest {
    public static void main(String[] args) {
        Flux.range(1, 10)
                .filter(i -> i > 5)
                .take(2)
                .next()//take only 6 as next is called
                .subscribe(System.out::println);
    }
}
