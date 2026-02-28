package org.spring.HandleTransformErrorDo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ErrorHandling {
    public static final Logger log = LoggerFactory.getLogger(ErrorHandling.class);
    public static void main(String[] args) {
        log.info("========onErrorReturn========");
        log.info("========Used for hardcoded fallback value in case of error ========");
        onErrorReturn();
        log.info("========onErrorResume========");
        log.info("========Used for custom fallback value or external fallback handler in case of error ========");
        onErrorResume();
        log.info("========onErrorComplete========");
        log.info("========Used for marking the execution as complete in case of error ========");
        onErrorComplete();
        log.info("========onErrorContinue========");
        log.info("========Used for continuing the execution even in case of error by skipping the error ========");
        onErrorContinue();
    }

    private static void onErrorReturn(){
        //onErrorReturn used for fallback value in case of error
        Flux.range(1,10)
                .map(i-> i==5 ? i/0 : i)
                .onErrorReturn(IllegalArgumentException.class,-2)//conditional fallback based on Exception type
                .onErrorReturn(error-> error.getMessage().contains("/ by zero"),
                        -3)//conditional fallback based on predicate
                .onErrorReturn(-1) // returning -1 if error occurs
                .subscribe(Util.subscriber());

    }

    private static void onErrorResume(){
        //onErrorResume used for custom fallback handling like calling external service in case of error
       //is basically Intercepts an error and switches to another Publisher instead of terminating
        Flux.range(1,10)
                .map(i-> i==5 ? i/0 : i)
                .onErrorResume(ex->fallback())//
                .subscribe(Util.subscriber());

    }

    private static void onErrorComplete(){
        //in case of error if we want to mark  the execution as complete without handling the error
        //is basically Intercepts an error and switches to another Publisher instead of terminating
        Flux.range(1,10)
                .map(i-> i==5 ? i/0 : i)
                .onErrorComplete(IllegalArgumentException.class) // based on exception type
                .onErrorComplete(error-> error.getMessage().contains("/ by zero"))// based on predicate
                .onErrorComplete()// default
                .subscribe(Util.subscriber());

    }

    private static void onErrorContinue(){
        //in case of error if we want to ignore the error and continue execution
        Flux.range(1,10)
                .map(i-> i==5 ? i/0 : i)
                .onErrorContinue((e,v)->{})//just skip the error
                .onErrorContinue((ex,obj)-> log.error("==> {}",obj,ex)) // print the error
                .subscribe(Util.subscriber());

    }


    private static Mono<Integer> fallback(){
        return Mono.fromSupplier(()-> Util.faker().random().nextInt(11,99));
    }
}
