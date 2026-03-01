package org.spring.unitTesting;

import org.junit.jupiter.api.Test;
import org.spring.common.Util;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class RangeTest {

    private Flux<Integer> getItems() {
        return Flux.range(1, 50);
    }

    private Flux<Integer> getRandomItems() {
        return Flux.range(1, 50)
                   .map(_ -> Util.faker().random().nextInt(1, 100));//50 random integers between 1 and 100
    }

    @Test
    public void rangeTest1() {
        StepVerifier.create(getItems())
                    .expectNext(1, 2, 3)//expect  1 -> 3 in order
                    .expectNextCount(47)//expect 47 items after 3rd  item received from above i.e. 50
                    .expectComplete()
                    .verify();
    }

    @Test
    public void rangeTest2() {
        StepVerifier.create(getItems())
                    .expectNext(1, 2, 3)//expect 1 -> 3 in order
                    .expectNextCount(22)// expect 22 after 3rd item after 3 i.e 25
                    .expectNext(26, 27, 28)//then expect 26 -> 28 after 22 items which 25
                    .expectNextCount(22)// 22 items after 28 i.e. 50
                    .expectComplete()
                    .verify();
    }

    @Test
    public void rangeTest3() {
        StepVerifier.create(getRandomItems())
                    .expectNextMatches(i -> i > 0 && i < 101)//checks condition only for 1st item
                    .expectNextCount(49)//expect only if above matches
                    .expectComplete()
                    .verify();
    }

    @Test
    public void rangeTest4() {
        StepVerifier.create(getRandomItems())
                //checks condition for all 50 instead of 1 item unlike above test
                    .thenConsumeWhile(i -> i > 0 && i < 101)
                    .expectComplete()
                    .verify();
    }

}