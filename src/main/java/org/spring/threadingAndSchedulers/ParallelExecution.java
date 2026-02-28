package org.spring.threadingAndSchedulers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

/**
 * This is recommended only for CPU heavy work
 * Not recommended for I/O task.
 * */
public class ParallelExecution {
    private static final Logger log = LoggerFactory.getLogger(ParallelExecution.class);

    static void main() {
        Flux.range(1, 10)
                .parallel()// we can also specify no of tasks in param e.g. parallel(3)
                .runOn(Schedulers.parallel())
                .map(ParallelExecution::timeConsumingProcess)
                .sequential()//we can also specify sequential execution after certain step
                .map(i->i+"a")
                .subscribe(Util.subscriber());

        Util.sleepSeconds(3);
    }

    private static int timeConsumingProcess(int i){
        log.info("Time consuming processes {}", i);
        Util.sleepSeconds(1);
        return i*2;

    }
}
