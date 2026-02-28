package org.spring.backpressure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;


//Usually Flux.generate by default handles back pressure
//But we want to handle manually we can use limitRate(n)
public class LimitRate {
    private static final Logger logger = LoggerFactory.getLogger(LimitRate.class.getName());
    static void main() {
        var producer = Flux.generate(
                ()->1,
                (state,sink) -> {
                    logger.info("generating {}", state);
                    sink.next(state);
                    return ++state;
                }
        ).cast(Integer.class)
                .subscribeOn(Schedulers.parallel());

        producer.limitRate(1)// only 5 data will be produced at time and resume once all 5 consumed
                .publishOn(Schedulers.boundedElastic())
                .map(LimitRate::timeConsumingTask)
                .subscribe(Util.subscriber());

        Util.sleepSeconds(60);
    }

    private static int timeConsumingTask(int u){
        logger.info("timeConsumingTask {}", u);
        Util.sleepSeconds(1);
        return u;
    }
}
