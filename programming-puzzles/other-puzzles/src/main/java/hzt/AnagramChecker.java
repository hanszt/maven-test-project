package hzt;

import org.hzt.utils.strings.StringX;

import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

public final class AnagramChecker {

    private static final Map<Character, Integer> letterToPrimeMap = Map.ofEntries(
            Map.entry('a', 2),
            Map.entry('b', 3),
            Map.entry('c', 5),
            Map.entry('d', 7),
            Map.entry('e', 11),
            Map.entry('f', 13),
            Map.entry('g', 17),
            Map.entry('h', 19),
            Map.entry('i', 23),
            Map.entry('j', 29),
            Map.entry('k', 31),
            Map.entry('l', 37),
            Map.entry('m', 41),
            Map.entry('n', 43),
            Map.entry('o', 47),
            Map.entry('p', 53),
            Map.entry('q', 59),
            Map.entry('r', 61),
            Map.entry('s', 67),
            Map.entry('t', 71),
            Map.entry('u', 73),
            Map.entry('v', 79),
            Map.entry('w', 83),
            Map.entry('x', 89),
            Map.entry('y', 97),
            Map.entry('z', 101)
    );

    private AnagramChecker() {
    }

    /**
     * time complexity: O(n * log(n))
     */
    public static boolean areAnagramsByGrouping(CharSequence s1, CharSequence s2) {
        final var s1grouping = StringX.of(s1).group();
        final var s2grouping = StringX.of(s2).group();
        return s1grouping.equals(s2grouping);
    }

    /**
     * time complexity: O(n * log(n))
     */
    public static boolean areAnagramsBySorting(CharSequence s1, CharSequence s2) {
        if (s1.length() != s2.length()) {
            return false;
        }
        final var s1CharsSorted = s1.chars().sorted().toArray();
        final var s2CharsSorted = s2.chars().sorted().toArray();
        return IntStream.range(0, s2CharsSorted.length).noneMatch(i -> s1CharsSorted[i] != s2CharsSorted[i]);
    }

    /**
     * time complexity: O(n)
     */
    public static boolean areAnagramsByPrimes(CharSequence s1, CharSequence s2) {
        return toPrimeProduct(s1) == toPrimeProduct(s2);
    }

    private static long toPrimeProduct(CharSequence s1) {
        return s1.chars()
                .filter(c -> c != ' ')
                .mapToLong(AnagramChecker::letterToPrimeOrThrow)
                .reduce(1, AnagramChecker::toProductOrThrowIfOverflow);
    }

    private static long toProductOrThrowIfOverflow(long acc, long n) {
        final long l = acc * n;
        if (l < 0) {
            throw new IllegalStateException(acc + " * " + n + " resulted in an overflow state");
        }
        return l;
    }

    private static long letterToPrimeOrThrow(int i) {
        final char character = (char) i;
        final Integer prime = letterToPrimeMap.get(character);
        Objects.requireNonNull(prime, "prime not found for character '" + character + "'");
        return prime.longValue();
    }
}
