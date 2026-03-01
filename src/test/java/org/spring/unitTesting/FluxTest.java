package org.spring.unitTesting;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxTest {

    private Flux<Integer> getItems() {
        return Flux.just(1, 2, 3)
                   .log();
    }

    @Test
    public void fluxTest1() {
        StepVerifier.create(getItems(), 1)//requesting for 1 item
                    .expectNext(1)
                    .thenCancel()//stop the test as no complete signal we will get as only asked for 1 item
                    .verify();
    }

    @Test
    public void fluxTest2() {
        StepVerifier.create(getItems())
                    .expectNext(1)// expect the data in particular order
                    .expectNext(2)
                    .expectNext(3)
                    .expectComplete()
                    .verify();
    }

    @Test
    public void fluxTest3() {
        StepVerifier.create(getItems())
                    .expectNext(1, 2, 3)//shorter version of above test
                    .expectComplete()
                    .verify();
    }

}