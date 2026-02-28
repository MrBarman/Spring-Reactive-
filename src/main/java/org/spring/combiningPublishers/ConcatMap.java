package org.spring.combiningPublishers;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
💥 concatMap() -->
        👉 Subscribes to inner publishers one by one
        👉 Waits for each to complete
        👉 Order strictly preserved

 ✅ Pros -->
        👉 Order guaranteed
        👉Safe for DB/API calls

 ❌ Cons -->
       👉Slower (no concurrency)
 **/
public class ConcatMap {
    static void main(String[] args) throws InterruptedException {
        //The 1 means:
        //          “Wait until countDown() is called once before continuing.”
        CountDownLatch latch = new CountDownLatch(1);

        Flux.just(1, 2, 3)
                .concatMap(i -> Flux.just(i)
                        .delayElements(Duration.ofMillis(100 - i * 20))
                )
                .doOnComplete(latch::countDown) // reduce counter to zero after complete
                .subscribe(System.out::println);

        // block main thread until this is called
        latch.await();

    }
}
