package hzt.reactive.own_implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * <a href="https://youtu.be/_stAxdjx8qk?t=662">Don’t use Reactive Streams in Java 9+ - Jacek Kunicki</a>
 * Don't implement reactive streams api yourself for real world applications
 * This is just a simple implementation out of curiosity
 */
public class Launcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    public static void main(String... args) {
        final var simplePublisher = new SimplePublisher(10);
        final var subscriber = new SimpleSubscriber();
        simplePublisher.subscribe(subscriber);
        Flow.Subscription subscription = simplePublisher.new SimpleSubscription(subscriber);
        subscription.request(10);
        //implementation of java, synchronous, not really useful
        SubmissionPublisher<Integer> s = new SubmissionPublisher<>();
        LOGGER.info("{}", s);
    }
}
