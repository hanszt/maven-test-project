package hzt;

import org.junit.jupiter.api.Test;

import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PrimitiveStreamsTest {

    @Test
    void testIntStreamSample() {
        IntSummaryStatistics intSummaryStatistics = IntStream.of(1, 6, 34, 8, 3, 0, 5).summaryStatistics();

        assertAll(
                () -> assertEquals(34, intSummaryStatistics.getMax()),
                () -> assertEquals(0, intSummaryStatistics.getMin()),
                () -> assertEquals(7, intSummaryStatistics.getCount())
        );
    }

    @Test
    void testDoubleStreamSample() {
        DoubleSummaryStatistics doubleSummaryStatistics = DoubleStream.of(1, 6, 34, 8.5, 3, 0, 5).summaryStatistics();

        assertAll(
                () -> assertEquals(34, doubleSummaryStatistics.getMax()),
                () -> assertEquals(0, doubleSummaryStatistics.getMin()),
                () -> assertEquals(7, doubleSummaryStatistics.getCount())
        );
    }
}
