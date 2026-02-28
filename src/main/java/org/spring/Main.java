package org.spring;

import org.spring.publisherSubscriber.publisher.PublisherImpl;
import org.spring.publisherSubscriber.subscriber.SubscriberImpl;

import java.time.Duration;

public class Main {
    public static void main(String[] args) throws InterruptedException {
      //  System.out.println("Hello world!");
        publisherProduceDataOnlySubscriberRequest();
       // publisherProducesOnlyRequestedItems();
       // subscriberCanCancelSubscription();
        producerCanSendError();
    }

    private static void publisherProduceDataOnlySubscriberRequest(){
        var publisher = new PublisherImpl();
        var subscriber =  new SubscriberImpl();
        publisher.subscribe(subscriber);
    }

    private static void publisherProducesOnlyRequestedItems() throws InterruptedException {
        var publisher = new PublisherImpl();
        var subscriber =  new SubscriberImpl();
        publisher.subscribe(subscriber);

        subscriber.getSubscription().request(5);
        Thread.sleep(Duration.ofSeconds(1));
        subscriber.getSubscription().request(5);
        Thread.sleep(Duration.ofSeconds(1));
        subscriber.getSubscription().request(5);
        Thread.sleep(Duration.ofSeconds(1));
        subscriber.getSubscription().request(5);
        Thread.sleep(Duration.ofSeconds(1));
        subscriber.getSubscription().request(5);
        Thread.sleep(Duration.ofSeconds(1));
    }

    private static void subscriberCanCancelSubscription() throws InterruptedException {
        var publisher = new PublisherImpl();
        var subscriber =  new SubscriberImpl();
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(5);
        Thread.sleep(Duration.ofSeconds(1));
        subscriber.getSubscription().cancel();
    }

    private static void producerCanSendError() throws InterruptedException {
        var publisher = new PublisherImpl();
        var subscriber =  new SubscriberImpl();
        publisher.subscribe(subscriber);
        subscriber.getSubscription().request(5);
        Thread.sleep(Duration.ofSeconds(1));
        subscriber.getSubscription().request(18);
    }
}