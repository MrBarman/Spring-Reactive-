package org.spring.fluxOperators;

import org.spring.common.Util;
import org.spring.publisherSubscriber.subscriber.SubscriberImpl;
import reactor.core.publisher.Flux;

public class FluxCreate {
    public static void main(String[] args) {
        var sub = new SubscriberImpl();
        //create flux programmatically
        //IT IS DESIGNED TO BE USED WHEN WE HAVE A SINGLE SUBSCRIBER
        //IT IS SYNCHRONIZED
//        Flux.create(fluxSink -> {
//            for(int i=0;i<10;i++) {
//                fluxSink.next(Util.faker().name().firstName());
//            }
//             fluxSink.complete();
//        }).subscribe(Util.subscriber());

        //calls next only once
        //for loop cannot be used onNext
        // rather use generate counter
        Flux.generate(
                () -> 0, //return type and 1st param
                (item, fluxSink) -> {
                    var name = Util.faker().name().firstName();
                    fluxSink.next(name);
                    item++;
                    if(item==7) {
                        fluxSink.complete();
                    }
                    return item;

                }).subscribe(Util.subscriber());


        //create flux on demand
        // must call request explicitly
        //onNext can be called as many times as we want
        Flux.<String>create(fluxSink -> {
            fluxSink.onRequest(request -> {
                for (int i = 0; i < request && !fluxSink.isCancelled(); i++) {
                    var name = Util.faker().name().firstName();
                    fluxSink.next(name);
                }
            });
        }).subscribe(sub);

        Util.sleepSeconds(2);
        sub.getSubscription().request(2);
        sub.getSubscription().cancel();
    }


}
