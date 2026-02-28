package org.spring.hotColdPublishers;

import reactor.core.publisher.Flux;

public class Test {
    static void main() {
        printCreated();
        System.out.println("===============================");
        printCreatedRefcount();
    }
    private static void printCreated() {
        Flux<Integer> flux = Flux.create(fluxSink -> {
            System.out.println("created");
            for (int i = 0; i < 5; i++) {
                fluxSink.next(i);
            }
        });
        Flux<Integer> map = flux.map(j -> j * 2);
        //print j value
        map.subscribe(System.out::println);
        map.subscribe(System.out::println);
    }

    private static void printCreatedRefcount() {
        Flux<Object> flux = Flux.create(fluxSink -> {
                    System.out.println("created");
                    for (int i = 0; i < 5; i++) {
                        fluxSink.next(i);
                    }
                    fluxSink.complete();
                })
                .publish()
                .refCount(2);
        flux.subscribe(System.out::println);
        flux.subscribe(System.out::println);
        // won't work as subcount will become 0 again and next subscribe will only increase 1
        flux.subscribe(System.out::println);
    }
}
