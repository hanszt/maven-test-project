package hzt.stream.utils;

import hzt.stream.StreamUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;

public final class MyCollections {

    private MyCollections() {
    }

    public static <T> Set<T> intersect(Iterable<? extends Collection<T>> collections) {
        Set<T> common = new HashSet<>();
        Iterator<? extends Collection<T>> iterator = collections.iterator();
        if (iterator.hasNext()) {
            common.addAll(iterator.next());
            while (iterator.hasNext()) {
                common.retainAll(iterator.next());
            }
        }
        return common;
    }

    public static <E> List<E> listOfIterable(Iterable<E> iterable) {
        return StreamUtils.streamOf(iterable).toList();
    }

    /**
     * http://www.java2s.com/example/java/lambda-stream/implement-the-collect-function-on-listt.html
     */
    public static <T, A, R> R collect(Iterable<T> iterable, Collector<T, A, R> collector) {
        A result = collector.supplier().get();
        iterable.forEach(t -> collector.accumulator().accept(result, t));
        return collector.finisher().apply(result);
    }
}
