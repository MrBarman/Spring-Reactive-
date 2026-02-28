package org.spring.combiningPublishers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;

public class StartsConcatMerge {
    /**
     💥startWith vs concatWith vs merge
        👉  All four are about combining publishers,
            but they behave very differently in terms of order, timing, and concurrency
            in Project Reactor (used in Spring WebFlux).

     * Let’s break them down clearly.
     * 1️⃣ startWith() -->
                👉 Adds values or another publisher before the current one. e.g ->
      💻Flux.just(3, 4)
          .startWith(1, 2)
          .subscribe(System.out::println);

      👉 Output -->
            1
            2
            3
            4
     *
     * Key Point  -->
            👉Prepends values
            👉No concurrency
            👉Order preserved
            👉Think: “prefix” operator
     ===========================================================
     * 2️⃣ concatWith() -->
               👉 Runs one publisher after the other completes e.g.
     💻 Flux.just(1, 2)
          .concatWith(Flux.just(3, 4))
          .subscribe(System.out::println);

      👉Output -->
            1
            2
            3
            4

     * Key Point -->
          ⚙️Accepts publisher
          👉Sequential
          👉Second starts only after first completes
          👉Order guaranteed
          👉No interleaving
          👉Think: “then do this”
     ========================================================
     * 3️⃣ merge() -->
            👉 Subscribes to multiple publishers at the same time
            👉 Emits values as they arrive e.g.

     💻 Flux.merge(
          Flux.interval(Duration.ofMillis(100)).take(2),
          Flux.interval(Duration.ofMillis(50)).take(2)
      ).subscribe(System.out::println);

     * 👉 Possible Output -->
               0
               0
               1
               1

      💰⚠ Order not guaranteed
      🪔 Faster publisher emits first.

     *  Key Point -->
            👉Concurrent
            👉Interleaved output
            👉Order NOT preserved
            👉Think: “parallel combine”

===================================================================
      🎯 Quick Comparison -->

     * Operator	    Concurrent?	    Order Preserved?	Use Case
     * startWith	    ❌       	    ✅	            Add prefix
     * concatWith	    ❌	            ✅	            Chain publishers
     * merge	        ✅	            ❌           	Parallel combine

     * 🎯 Interview One-Liner ----->>>>
             ⚙️startWith → prepend
             ⚙️concatWith → sequential combine, accepts publisher
             ⚙️merge → concurrent combine
     */

    private static final Logger log = LoggerFactory.getLogger(StartsConcatMerge.class);
    static void main() throws InterruptedException {
        log.info("Starting startWith()");
        CountDownLatch latchStartWith = new CountDownLatch(1);
        Flux.just(3, 4)
                .startWith(1, 2)//order preserved
                .doOnComplete(latchStartWith::countDown)
                .subscribe(System.out::println);
        log.info("========================");
        latchStartWith.await();
        log.info("Starting concatWith()");

        CountDownLatch latchConcatWith = new CountDownLatch(1);
        Flux.just(3, 4)
                .concatWith(Flux.just(2, 1))//order preserved
                .doOnComplete(latchConcatWith::countDown)
                .subscribe(System.out::println);
        log.info("========================");
        latchConcatWith.await();

        log.info("Starting merge()");
        CountDownLatch latchMerge = new CountDownLatch(1);
        Flux.merge(// no order will be preserved
                Flux.interval(Duration.ofMillis(100)).take(2),
                        Flux.just(3, 4)
                ).doOnComplete(()-> {
                    log.info("========================");
                    latchMerge.countDown();
                })
                .subscribe(System.out::println);
        latchMerge.await();
    }
}
