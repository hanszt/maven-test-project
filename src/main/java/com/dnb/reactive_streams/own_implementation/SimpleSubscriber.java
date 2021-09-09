package com.dnb.reactive_streams.own_implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Flow;

/**
 * https://youtu.be/_stAxdjx8qk?t=662
 */
public class SimpleSubscriber implements Flow.Subscriber<Integer> {

    private static final Logger LOGGER = LogManager.getLogger(SimpleSubscriber.class);

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        //No implementation
    }

    @Override
    public void onNext(Integer item) {
        LOGGER.info("item = {}", item);
    }

    @Override
    public void onError(Throwable throwable) {
        LOGGER.error("throwable: ", throwable);
    }

    @Override
    public void onComplete() {
        LOGGER.info("Completed!");
    }
}
