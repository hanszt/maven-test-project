package hzt.reactive.own_implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Flow;

/**
 * https://youtu.be/_stAxdjx8qk?t=662
 */
public class SimpleSubscriber implements Flow.Subscriber<Integer> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSubscriber.class);

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
