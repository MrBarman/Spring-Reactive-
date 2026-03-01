package org.spring.unitTesting;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class VirtualTimeTest {

    private Flux<Integer> getItems() {
        return Flux.range(1, 5)
                   .delayElements(Duration.ofSeconds(10));
    }

    // We can not run like this for time duration data as data will be emitted in interval of time
    // and this will cost a lot of time
   // @Test
    public void demo() {
        StepVerifier.create(getItems())
                    .expectNext(1, 2, 3, 4, 5)
                    .expectComplete()
                    .verify();
    }

    //so use virtual time
    @Test
    public void virtualTimeTest1() {
        StepVerifier.withVirtualTime(this::getItems)
                //virtually wait of 51 seconds as getItems returning 5 items with the interval of 10 seconds
                    .thenAwait(Duration.ofSeconds(51))
                    .expectNext(1, 2, 3, 4, 5)
                    .expectComplete()
                    .verify();

    }

    @Test
    public void virtualTimeTest2() {
        StepVerifier.withVirtualTime(this::getItems)
                // invoke on subscribe()
                // this is required as we are asking not to send anything till 9 sec
                    .expectSubscription()
                // no event for 1st 9 second
                    .expectNoEvent(Duration.ofSeconds(9))
                // wait for 1 second after 9 second
                    .thenAwait(Duration.ofSeconds(1))
                // get 1 after 1st interval of 10 seconds
                    .expectNext(1)
                // wait 40 seconds
                    .thenAwait(Duration.ofSeconds(40))
                // get rest of the items
                    .expectNext(2, 3, 4, 5)
                    .expectComplete()
                    .verify();

    }


}