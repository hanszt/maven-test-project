package hzt;

import java.util.Comparator;
import java.util.function.Function;

public final class HztComparators {

    private HztComparators() {
    }

    public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
            Function<T, ? extends U> keyExtractor, boolean ascending) {
        return ascending ? Comparator.comparing(keyExtractor) : Comparator.comparing(keyExtractor).reversed();
    }
}
