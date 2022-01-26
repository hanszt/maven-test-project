package hzt.utils;

import hzt.stream.StreamUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        return StreamUtils.streamOf(iterable).collect(Collectors.toUnmodifiableList());
    }

    /**
     * http://www.java2s.com/example/java/lambda-stream/implement-the-collect-function-on-listt.html
     */
    public static <T, A, R> R collect(Iterable<T> iterable, Collector<T, A, R> collector) {
        A result = collector.supplier().get();
        iterable.forEach(t -> collector.accumulator().accept(result, t));
        return collector.finisher().apply(result);
    }

    public static <T, R> List<R> map(@NotNull Iterable<T> collection, @NotNull Function<T, R> mapper) {
        List<R> resultList = new ArrayList<>();
        for (T t : collection) {
            resultList.add(mapper.apply(t));
        }
        return resultList;
    }

    public static <T> List<T> filter(@NotNull Iterable<T> collection, @NotNull Predicate<T> predicate) {
        List<T> resultList = new ArrayList<>();
        for (T t : collection) {
            if (predicate.test(t)) {
                resultList.add(t);
            }
        }
        return resultList;
    }

    public static <T, R> List<R> flatMap(@NotNull Iterable<Iterable<T>> iterables, @NotNull Function<T, R> mapper) {
        List<R> resultList = new ArrayList<>();
        for (var iterable : iterables) {
            for (T t : iterable) {
                resultList.add(mapper.apply(t));
            }
        }
        return resultList;
    }

    public static <T> List<T> distinct(@NotNull Iterable<T> iterable) {
        Set<T> set = new LinkedHashSet<>();
        iterable.forEach(set::add);
        return List.copyOf(set);
    }
}
