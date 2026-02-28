package org.spring.backpressure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

/**
    Reactor provides backpressure handling strategies
    - buffer
    - drop
    - latest
    - error
 =============================================================
 Backpressure Strategies
 🎯 BUFFER Strategy:
        Buffers all overflow items until the subscriber can process them.

    👉javaFlux.interval(Duration.ofMillis(1))
        .onBackpressureBuffer()  // Default: unbounded buffer
        .subscribe(data -> slowProcess(data));

 // With bounded buffer
    👉Flux.interval(Duration.ofMillis(1))
        .onBackpressureBuffer(100)  // Max 100 items in buffer
        .subscribe(data -> slowProcess(data));

 // With overflow strategy
    👉Flux.interval(Duration.ofMillis(1))
        .onBackpressureBuffer(
        100,  // Max buffer size
        dropped -> log.warn("Dropped: " + dropped),  // Callback
        BufferOverflowStrategy.DROP_OLDEST  // Drop oldest when full
        )
        .subscribe(data -> slowProcess(data));

 ✅When to use:
 ⚙️Temporary bursts of data
 ⚙️You can't afford to lose any data
 ⚙️You have enough memory

 ❌Risk: Unbounded buffer can cause OutOfMemoryError

 🎯 DROP Strategy:
        Drops new items when the consumer can't keep up.

 👉javaFlux.interval(Duration.ofMillis(1))
        .onBackpressureDrop()  // Drop excess items silently
        .subscribe(data -> slowProcess(data));

 // With callback to track dropped items
 👉Flux.interval(Duration.ofMillis(1))
        .onBackpressureDrop(dropped -> {
        log.warn("Dropped item: " + dropped);
        metrics.incrementDropped();
        })
        .subscribe(data -> slowProcess(data));

 ✅When to use:
 ⚙️Real-time data where latest is more important (stock ticks, sensor data)
 ⚙️Losing some data is acceptable
 ⚙️You want to prevent memory issues

 ✅Example use case:
 ⚙️// Live data (metrics, sensor data)
 👉sensorData
        .onBackpressureDrop(price -> log.debug("Skipped sensor data: " + data))
        .subscribe(data -> updateUI(data));

 🎯 LATEST Strategy:
        Keeps only the latest item, drops everything in between.

 👉javaFlux.interval(Duration.ofMillis(1))
        .onBackpressureLatest()  // Always keeps the most recent
        .subscribe(data -> slowProcess(data));


 ✅When to use:
 ⚙️Only the current state matters
 ⚙️Historical values are irrelevant
 ⚙️UI updates, live dashboards

 ✅Example use case:
 ⚙️Cricket score - only current score matters
 👉cricketScore
        .onBackpressureLatest()
        .subscribe(score -> displayScore(temp));

 ⚙️ Mouse position - only latest position matters
 👉mouseMovements
        .onBackpressureLatest()
        .subscribe(pos -> updateCursor(pos));

 🎯 ERROR Strategy:
        Throws an error when backpressure occurs (default behavior in Reactor).

 👉javaFlux.interval(Duration.ofMillis(1))
        .onBackpressureError()  // Explicit error on overflow
        .subscribe(
        data -> slowProcess(data),
        error -> log.error("Backpressure error!", error)
 );
 ✅When to use:
 ⚙️You want to fail fast and alert
 ⚙️Backpressure indicates a serious problem
 ⚙️You want to force proper flow control

 ✅Example use case:
 ⚙️Critical payment processing - must process everything or fail
 👉paymentStream
    .onBackpressureError()
    .subscribe(
    payment -> processPayment(payment),
    error -> {
    alertOps("Payment processing overwhelmed!");
    shutdownGracefully();
    }
 );


 📊 Comparison Table:

    Strategy        Behavior            Memory Impact           Data Loss               Use Case

 💥  BUFFER          Queue all items     High (unbounded)          None              Critical data, temporary bursts
 💥  DROP            Discard new items   Low                     Yes(new items)      R eal-time feeds, non-critical
 💥  LATEST          Keep only newest    Very low(1 item)        Yes (old items)     UI updates, current state
 💥  ERROR           Fail immediately    None                    N/A                 Fail-fast, alerts


 Practical Examples
 ⚙️Example 1: Log Processing
 // Buffer logs during bursts
 👉logStream
        .onBackpressureBuffer(1000)  // Buffer up to 1000 logs
        .flatMap(log -> writeToDatabase(log), 10)  // Process 10 concurrent
 .      subscribe();
 ⚙️Example 2: Live Stock Prices
// Only latest price matters
 👉stockTickerStream
        .onBackpressureLatest()  // Always show current price
        .subscribe(price -> updateStockDisplay(price));
 ⚙️Example 3: Sensor Data
 // Drop intermediate readings
 👉sensorStream
        .onBackpressureDrop(reading -> metrics.recordDropped())
        .buffer(Duration.ofSeconds(1))  // Batch per second
        .subscribe(batch -> processBatch(batch));
 ⚙️Example 4: Critical Orders
 // Fail if can't keep up
 👉orderStream
        .onBackpressureError()
        .doOnError(e -> sendAlert("Order processing overload!"))
        .retry(3)
        .subscribe(order -> processOrder(order));

 ⚙️Advanced: Combining Strategies
 👉javaFlux.interval(Duration.ofMillis(1))
        .onBackpressureBuffer(
            100,  // Small buffer for bursts
        dropped -> log.warn("Buffer full, dropped: " + dropped),
        BufferOverflowStrategy.DROP_LATEST  // Drop newest when buffer full
 )
 .flatMap(this::process, 5)  // Limit concurrency
 .subscribe();

 Choosing the Right Strategy
 Ask yourself:

 💥 Can I lose data?

 No → BUFFER (with bounded size)
 Yes → DROP or LATEST


 💥 Do I need all historical data?

 Yes → BUFFER
 No → LATEST


 💥 Should backpressure be an error condition?

 Yes → ERROR
 No → DROP/LATEST/BUFFER


 💥 Do I have memory constraints?

 Yes → DROP or LATEST
 No → BUFFER




 Default Behavior
 Without specifying a strategy:
// This will ERROR on backpressure
 👉Flux.interval(Duration.ofMillis(1))
        .subscribe(data -> slowProcess(data));
 // Exception: reactor.core.Exceptions$OverflowException:
 // Could not emit tick 256 due to lack of requests

 Key Takeaway

 💥 BUFFER = Save everything (memory risk)
 💥 DROP = Ignore new items (data loss)
 💥 LATEST = Keep only newest (data loss)
 💥 ERROR = Fail immediately (no loss, no processing)

 Choose based on your data criticality, memory constraints, and business requirements!
 */
public class BackPressureStrategies {

    private static final Logger log = LoggerFactory.getLogger(BackPressureStrategies.class);

    static void main(String[] args) {

        var producer = Flux.create(sink -> {
                               for (int i = 1; i <= 500 && !sink.isCancelled(); i++) {
                                   log.info("generating {}", i);
                                   sink.next(i);
                                   Util.sleep(Duration.ofMillis(50));
                               }
                               sink.complete();
                           })
                           .cast(Integer.class)
                           .subscribeOn(Schedulers.parallel());

        producer
                // .onBackpressureBuffer()
                // .onBackpressureError()
                // .onBackpressureBuffer(10)
                // .onBackpressureDrop()
                .onBackpressureLatest()
                .log()
                .limitRate(1)
                .publishOn(Schedulers.boundedElastic())
                .map(BackPressureStrategies::timeConsumingTask)
                .subscribe();


        Util.sleepSeconds(60);
    }

    private static int timeConsumingTask(int i) {
        log.info("received: {}", i);
        Util.sleepSeconds(1);
        return i;
    }



}