package org.spring.unitTesting;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class TimeoutTest {

    private Flux<Integer> getItems() {
        return Flux.range(1, 5)
                   .delayElements(Duration.ofMillis(200));
    }

    @Test
    public void timeoutTest() {
        StepVerifier.create(getItems())
                    .expectNext(1, 2, 3, 4, 5)
                    .expectComplete()
                //  only pass when receive all data within 1500 milliseconds else fail
                    .verify(Duration.ofMillis(1500));
    }

}