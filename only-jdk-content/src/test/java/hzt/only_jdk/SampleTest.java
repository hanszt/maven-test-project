package hzt.only_jdk;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class SampleTest {

    @Test
    void testStreamGeneration() {
        IntStream.range(0, 100).forEach(SampleTest::testSumOfRandomNumbers);
        }

    private static void testSumOfRandomNumbers(int i) {
        final var LIMIT_VALUE = 1000;
        final var ERROR = 40;
        final var sum = Stream.generate(Math::random)
                .limit(LIMIT_VALUE)
                .reduce(0D, Double::sum);
        System.out.println("sum = " + sum);
        final var HALF_LIMIT_VALUE = LIMIT_VALUE / 2.;
        assertTrue(sum < HALF_LIMIT_VALUE + ERROR);
        assertTrue(sum > HALF_LIMIT_VALUE - ERROR);
    }
}
