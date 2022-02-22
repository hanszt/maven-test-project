package hzt.reactive.rxjava;

import hzt.utils.It;
import io.reactivex.rxjava3.core.Flowable;

public class RxHelloWorld {

    public static void main(String[] args) {
        //noinspection ResultOfMethodCallIgnored
        Flowable.just("Hello world").subscribe(It::println);
    }
}
