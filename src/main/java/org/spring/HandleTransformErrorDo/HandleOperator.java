package org.spring.HandleTransformErrorDo;

import org.spring.common.Util;
import reactor.core.publisher.Flux;

public class HandleOperator {
    public static void main(String[] args) {
        //handle behaves like filter and map
        // it is useful when based multi condition response is need to send
        Flux<Integer> flux=  Flux.range(1,10);
        Flux<Integer> flux1 = flux.handle((item,sink)->{
            switch (item) {
               case 1 -> sink.next(-2);
               case 4 -> {}
               case 7 -> sink.error(new RuntimeException("Test ERR"));
                default -> sink.next(item);
            }
        });
        flux1.subscribe(Util.subscriber());
    }
}
