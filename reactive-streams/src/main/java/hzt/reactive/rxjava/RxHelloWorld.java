package hzt.reactive.rxjava;

import io.reactivex.rxjava3.core.Flowable;
import org.hzt.utils.It;

public class RxHelloWorld {

    public static void main(String[] args) {
        //noinspection ResultOfMethodCallIgnored
        Flowable.just("Hello world").subscribe(It::println);
    }
}
