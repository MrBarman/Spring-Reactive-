package org.spring.combiningPublishers;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

/**
 💥 flatMapSequential() -->
            👉 Subscribes to inner publishers concurrently
            👉 BUT emits results in original order

 ⚙️ Internally:
     👉All run in parallel
     👉Results buffered
     👉Emitted in order

 ✅ Pros:
        👉Concurrency + Order
 ❌ Cons:
        👉Uses buffering
 */
public class FlatMapSequential {
    static void main() throws InterruptedException {
        //The 1 means:
        //          “Wait until countDown() is called once before continuing.”
        CountDownLatch latch = new CountDownLatch(1);

        Flux.just(1, 2, 3)
                .flatMapSequential(i -> Flux.just(i)
                        .delayElements(Duration.ofMillis(100 - i * 20))
                )
                .doOnComplete(latch::countDown) // reduce counter to zero after complete
                .subscribe(System.out::println);

        // block main thread until this is called
        latch.await();
    }
}
