package org.spring.common;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultSubscriber<T> implements Subscriber<T> {

    private static final Logger log = LoggerFactory.getLogger(DefaultSubscriber.class);
    private final String name;

    //to know the subscriber name as
    //single publisher can be subscribed by multiple subscriber
    public DefaultSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        subscription.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(T t) {
        log.info("{} received: {}",this.name,t);
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("{} error: ",this.name, throwable);
    }

    @Override
    public void onComplete() {
        log.info("{} completed",this.name);
    }
}
