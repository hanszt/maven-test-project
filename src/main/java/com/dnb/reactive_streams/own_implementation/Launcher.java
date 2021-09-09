package com.dnb.reactive_streams.own_implementation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * https://youtu.be/_stAxdjx8qk?t=1523
 * Don't implement reactive streams api yourself for real world applications
 * This is just a simple implementation out of curiosity
 */
public class Launcher {

    private static final Logger LOGGER = LogManager.getLogger(Launcher.class);

    public static void main(String[] args) {
        final var simplePublisher = new SimplePublisher(10);
        final var subscriber = new SimpleSubscriber();
        simplePublisher.subscribe(subscriber);
        Flow.Subscription subscription = simplePublisher.new SimpleSubscription(subscriber);
        subscription.request(10);
        //implementation of java, synchronous, not really useful
        SubmissionPublisher<Integer> s = new SubmissionPublisher<>();
        LOGGER.info(s);
    }
}
