package org.hzt;

import org.eclipse.collections.api.map.primitive.IntIntMap;
import org.eclipse.collections.impl.map.mutable.primitive.IntIntHashMap;
import org.hzt.utils.PreConditions;

import java.util.stream.LongStream;

public final class StatisticsUtils {

    private StatisticsUtils() {
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
    public static IntIntMap benfordsDistribution(LongStream longStream) {
        return longStream.collect(IntIntHashMap::new,
                (map, value) -> map.addToValue(firstDigit(value), 1),
                IntIntHashMap::putAll);
    }

    public static int firstDigit(final long input) {
        PreConditions.require(input >= 0, () -> "x must be greater ot equal to 0. (was " + input + ")");
        long x = input;
        while (x > 9) {
            x /= 10;
        }
        return (int) x;
    }
}
