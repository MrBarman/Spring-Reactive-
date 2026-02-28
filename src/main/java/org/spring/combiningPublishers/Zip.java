package org.spring.combiningPublishers;

import org.spring.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 💥 Zip
    - we will subscribe to all the producers at the same time
    - all or nothing
    - all producers will have to emit an item
    - Flux.zip(flux1, flux2)

 💥 zipWith → same but instance method
             ❌ Instead of: Flux.zip(flux1, flux2)
             ✅ You write: flux1.zipWith(flux2)

 💥 zipWhen → dependent async combination
            👉 Used when second publisher depends on first value.
            👉 This is very common in WebFlux service calls.
            Example:
                Mono<User> userMono = getUser(id);
                    userMono.zipWhen(user ->
                                        getOrders(user.getId()))
                                        .subscribe(System.out::println);
        👉What Happens
                ✅First Mono emits User
                ✅That value is used to trigger second async call
                ✅Result becomes: Tuple2<User, Orders>
        ✅ Sequential dependency
        ✅Clean reactive chaining

 💥  combineLatest → emit whenever any source updates
                    👉 Emits when ANY source emits
                    👉 Combines latest values from all sources

 🎯 Interview One-Liners
            ✅ zip → combine element by element
            ✅ zipWith → same but instance method
            ✅ zipWhen → dependent async combination
            ✅ combineLatest → emit whenever any source updates
 */
public class Zip {

    record Car(String body, String engine, String tires){}

    public static void main(String[] args) {

        Flux.zip(getBody(), getEngine(), getTires())
                .map(t -> new Car(t.getT1(), t.getT2(), t.getT3()))
                .subscribe(Util.subscriber());

        Util.sleepSeconds(5);
    }

    private static Flux<String> getBody(){
        return Flux.range(1, 5)
                   .map(i -> "body-" + i)
                   .delayElements(Duration.ofMillis(100));
    }

    private static Flux<String> getEngine(){
        return Flux.range(1, 3)
                   .map(i -> "engine-" + i)
                   .delayElements(Duration.ofMillis(200));
    }

    private static Flux<String> getTires(){
        return Flux.range(1, 10)
                   .map(i -> "tires-" + i)
                   .delayElements(Duration.ofMillis(75));
    }


}