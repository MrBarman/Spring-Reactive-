package org.spring.combiningPublishers;

import org.spring.combiningPublishers.services.OrderService;
import org.spring.combiningPublishers.services.UserService;
import org.spring.common.Util;

/*
    Sequential non-blocking IO calls!
    flatMap is used to flatten the inner publisher / to subscribe to the inner publisher
    Mono is supposed to be 1 item - what if the flatMap returns multiple items!?
    use flatMapMany in that case
    basically Each input produces MULTIPLE outputs
    1 → Many relationship
 */
public class FlatMapMany {

    static void main(String[] args) {

        /*
            We have username
            get all user orders!
         */

        UserService.getUserId("jake")
                   .flatMapMany(OrderService::getUserOrders)
                   .subscribe(Util.subscriber());


        Util.sleepSeconds(3);
    }

}