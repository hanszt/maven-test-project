package hzt.only_jdk;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RandomTests {

    @SuppressWarnings("squid:S5977")
    private static final Random RANDOM = new Random();

    @Test
    void testRandomIntStream() {
        final var ints = RANDOM.ints()
                .takeWhile(i -> i < Integer.MAX_VALUE - 100_000_000)
                .toArray();

        System.out.println(Arrays.toString(ints));

        assertNotNull(ints);
    }

    @Test
    void testRandomDoubles() {
        final var doubles = RANDOM.doubles(10).toArray();

        System.out.println(Arrays.toString(doubles));

        assertEquals(10, doubles.length);
    }
}
