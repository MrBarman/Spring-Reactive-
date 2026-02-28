package org.spring.mono;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class LazyStreamBehaviour01 {
    private static final Logger log = LoggerFactory.getLogger(LazyStreamBehaviour01.class);

    public static void main(String[] args) {
        Stream.of(1).peek(i->log.info("peek Val {}",i)).toList();
    }
}
