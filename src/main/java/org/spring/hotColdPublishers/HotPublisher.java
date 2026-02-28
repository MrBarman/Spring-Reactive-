package org.spring.hotColdPublishers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * 🎯 Quick Mental Model
 *  share() i.e. publish().refCount(1) --> hot but stops when no subscribers
 *  publish().autoConnect() → hot and stays alive (take operator won't stop publishing)
      💥 For above two, past data cannot be consumed by subscriber if joined late
      💥 For past data replace publish() with replace() -> by default will replay every thing.
      💥 To get required past data we can give param value e.g.  replay(n).autoConnect(n)
 *  Sinks → truly hot, emits even with zero subscribers
 */

public class HotPublisher {
    private static final Logger log = LoggerFactory.getLogger(HotPublisher.class);

    /**
     * hot publishers are those publishers which share state with multiple subscribers
     * different subscriber won't have personalized data (think of live broadcast)
     * Use cases involve --->
     * WebSocket streaming
     * Live notifications
     * Stock prices
     * Event broadcasting
     */
    static void main() {
      /*
       * You can convert Cold publisher to hot using:
       * share()
            👉share() = publish().refCount(1) -> 1 is min subscriber required to start  emitting
            👉 It starts emitting only when the first subscriber connects
            👉 If all subscribers cancel, it stops
            👉 When a new subscriber comes later, it restarts
       * publish().refCount() --> explained above
       * publish().autoConnect(n)
                 👉converts a cold publisher into a hot publisher and starts emitting after n subscribers connect.
                    If n=0. it starts emitting without being subscribed
                 👉Unlike refCount, it does not stop when subscribers cancel.
                 💥Use autoConnect(n) when:
                    👉You want to start streaming once enough clients join
                    👉You don’t want it to stop even if subscribers leave
                    👉Long-running streams like stock feeds
       * cache()
       * Sinks.many() (advanced)
       */
        var dataFlux = streamData().share();
        Util.sleepSeconds(2);
        dataFlux.take(2).subscribe(Util.subscriber("sub1"));

        Util.sleepSeconds(3);
        dataFlux.take(3).subscribe(Util.subscriber("sub2"));
        Util.sleepSeconds(16);
    }


    private static Flux<String> streamData() {
        return Flux.generate(() -> {
                            log.info("received request");
                            return 1;
                        },
                        (state, sink) -> {
                            var data = "data" + state;
                            sink.next(data);
                            log.info("data: {}", data);
                            return ++state;
                        }
                ).take(10)
                .delayElements(Duration.ofSeconds(1))
                .cast(String.class);
    }
}
