package hzt.stream.utils;

import hzt.stream.StreamUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public final class MyCollections {

    private MyCollections() {
    }

    public static <T> Set<T> intersect(Collection<? extends Collection<T>> collections) {
        Set<T> common = new HashSet<>();
        if (!collections.isEmpty()) {
            Iterator<? extends Collection<T>> iterator = collections.iterator();
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
}
