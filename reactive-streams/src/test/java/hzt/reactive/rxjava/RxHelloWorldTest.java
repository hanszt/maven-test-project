package hzt.reactive.rxjava;

import hzt.numbers.IntX;
import hzt.utils.It;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RxHelloWorldTest {

    @Test
    void testRxHelloWorld() {
        final var disposable = Flowable.just("Hello world").subscribe(It::println);
        assertTrue(disposable::isDisposed);
    }

    @Test
    void testFlowableRangeFilterMap() {
        final var disposable = Flowable.range(1, 100)
                .filter(IntX::isEven)
                .map(String::valueOf)
                .subscribe(It::println);

        assertTrue(disposable::isDisposed);
    }

    @Test
    void testFlowableRangeFilterMapSubscribeWithErrorChannel() {
        RuntimeException exception = new IllegalStateException();

        final var disposable = Observable.create(this::evenMillis)
                .map(i -> "hallo" + i)
                .subscribe(It::println, exception::addSuppressed);

        assertAll(
                () -> assertTrue(disposable::isDisposed),
                () -> assertEquals(1, exception.getSuppressed().length)
        );
    }

    private void evenMillis(ObservableEmitter<Long> emitter) {
        while (!emitter.isDisposed()) {
            long time = System.currentTimeMillis();
            emitter.onNext(time);
            if (time % 2 != 0) {
                emitter.onError(new IllegalStateException("Odd millisecond!"));
                break;
            }
        }
    }
}
