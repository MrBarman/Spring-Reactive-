package org.spring.mono;

import org.spring.publisherSubscriber.subscriber.SubscriberImpl;
import reactor.core.publisher.Mono;

public class MonoJust {
    public static void main(String[] args) {
        var data = Mono.just("data");
        var subscriber = new SubscriberImpl();
        data.subscribe(subscriber);
        subscriber.getSubscription().request(1);
        subscriber.getSubscription().cancel();
    }
}
