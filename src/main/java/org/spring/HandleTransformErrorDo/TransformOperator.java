package org.spring.HandleTransformErrorDo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spring.common.Util;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class TransformOperator {
    private static final Logger log = LoggerFactory.getLogger(TransformOperator.class);

    record Customer (int id, String name){}
    record PurchaseOrder(String productName, int price, int quantity){}
    public static void main(String[] args) {
        //transform operator is good for removing repetitive work
        //we basically can create own operator
        //transform accepts a function returns flux/mono
        getCustomers()
                .transform(addDebugger())
                .subscribe();

        getPurchaseOrders()
                .transform(addDebugger())
                .subscribe();

        //transform without method
        Flux.just(1, 4, 6, 7, 9, 12)
                .transform(addDebugger)
                .subscribe();
    }
    private static Flux<Customer> getCustomers(){
        return Flux.range(1,3)
                .map(i->new Customer(i, Util.faker().name().firstName()));
    }

    private static Flux<PurchaseOrder> getPurchaseOrders(){
        return Flux.range(1,5)
                .map(i->new PurchaseOrder(Util.faker().commerce().productName(), i,i*10));
    }

    //we can replace below method with Unary operator as return type is same as accept type
    //private static <T> UnaryOperator<Flux<T>
    private static <T> Function<Flux<T>,Flux<T>> addDebugger(){
        return tFlux -> tFlux
                .doOnNext(i->log.info("received: {}",i))
                .doOnComplete(()->log.info("completed"))
                .doOnError(err-> log.error("Error: ",err));

    }

    //<T> will not work here
    static Function<Flux<Integer>,Flux<Integer>> addDebugger =
       tFlux -> tFlux
                .doOnNext(i->log.info("received: {}",i))
                .doOnComplete(()->log.info("completed"))
                .doOnError(err-> log.error("Error: ",err));


}
