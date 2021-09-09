package com.dnb.reactive_streams.own_implementation;

import java.util.Iterator;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * https://youtu.be/_stAxdjx8qk?t=595
 */
public class SimplePublisher implements Flow.Publisher<Integer> {

    private final Iterator<Integer> iterator;

    public SimplePublisher(int count) {
        this.iterator = IntStream.rangeClosed(1, count).iterator();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super Integer> subscriber) {
        iterator.forEachRemaining(subscriber::onNext);
        subscriber.onComplete();
    }

    /**
     * https://youtu.be/_stAxdjx8qk?t=950
     */
    public class SimpleSubscription implements Flow.Subscription {

        private final Flow.Subscriber<? super Integer> subscriber;
        private final AtomicBoolean terminated = new AtomicBoolean(false);

        public SimpleSubscription(Flow.Subscriber<? super Integer> subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void request(final long n) {
            if (n <= 0) {
                subscriber.onError(new IllegalArgumentException());
            }
            for (long demand = n; demand > 0 && iterator.hasNext() && !terminated.get(); demand--) {
                subscriber.onNext(iterator.next());
            }
            if (!iterator.hasNext() && !terminated.getAndSet(true)) {
                subscriber.onComplete();
            }
        }

        @Override
        public void cancel() {
            terminated.set(true);
        }
    }
}
