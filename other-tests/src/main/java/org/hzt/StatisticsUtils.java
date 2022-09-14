package org.hzt;

import org.eclipse.collections.api.map.primitive.IntIntMap;
import org.eclipse.collections.api.map.primitive.ObjectIntMap;
import org.eclipse.collections.api.tuple.primitive.IntIntPair;
import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;
import org.eclipse.collections.impl.map.mutable.primitive.ObjectIntHashMap;
import org.hzt.utils.PreConditions;
import org.hzt.utils.sequences.Sequence;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.LongToIntFunction;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public final class StatisticsUtils {

    private StatisticsUtils() {
    }

    public static IntIntMap groupedCounts(LongToIntFunction selector, LongStream longStream) {
        return longStream.collect(IntIntHashMap::new,
                (map, value) -> map.addToValue(selector.applyAsInt(value), 1),
                StatisticsUtils::combineGroupedCounts);
    }

    public static <T, K> ObjectIntMap<K> groupedCounts(Function<? super T, ? extends K> selector, Stream<T> stream) {
        return stream.collect(ObjectIntHashMap::new,
                (map, value) -> map.addToValue(selector.apply(value), 1),
                StatisticsUtils::combineGroupedCounts);
    }

    private static void combineGroupedCounts(IntIntHashMap m1, IntIntHashMap m2) {
        for (final var entry : m1.keyValuesView()) {
            final var keyMap1 = entry.getOne();
            if (m2.containsKey(keyMap1)) {
                m2.addToValue(keyMap1, entry.getTwo());
            }
        }
    }

    private static <K> void combineGroupedCounts(ObjectIntHashMap<K> m1, ObjectIntHashMap<K> m2) {
        for (final var entry : m1.keyValuesView()) {
            final var keyMap1 = entry.getOne();
            if (m2.containsKey(keyMap1)) {
                m2.addToValue(keyMap1, entry.getTwo());
            }
        }
    }

    public static int firstDigit(final long input) {
        PreConditions.require(input >= 0, () -> "x must be greater ot equal to 0. (was " + input + ")");
        long x = input;
        while (x > 9) {
            x /= 10;
        }
        return (int) x;
    }

    /**
     * Benford's law, also known as the Newcomb–Benford law, the law of anomalous numbers, or the first-digit law,
     * is an observation that in many real-life sets of numerical data, the leading digit is likely to be small.
     *
     * @param longStream a stream of longs to test benfords law on
     * @return an int-int-map where the key is the first digit of each nr in the stream
     * and the value the amount of times a long starts with that first digit.
     * @see <a href="https://en.wikipedia.org/wiki/Benford%27s_law">Benford's law</a>
     */
    public static boolean obeysBenfordsLaw(LongStream longStream) {
        return obeysBenfordsLaw(groupedCounts(StatisticsUtils::firstDigit, longStream));
    }

    public static boolean obeysBenfordsLaw(IntIntMap firstDigitToCounts) {
        final var array = Sequence.of(firstDigitToCounts.keyValuesView())
                .sorted(Comparator.comparingInt(IntIntPair::getTwo).reversed())
                .mapToInt(IntIntPair::getOne)
                .toArray();

        final var sortedArray = Arrays.stream(array).sorted().toArray();
        return Arrays.equals(sortedArray, array);
    }
}
