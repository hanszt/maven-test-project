package hzt;

import org.junit.jupiter.api.Test;

import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpecializedStreamsTest {

    private final SpecializedStreams specializedStreams = new SpecializedStreams();

    @Test
    void testIntStreamSample() {
        IntSummaryStatistics intSummaryStatistics = specializedStreams.intsSummaryStatistics(1, 6, 34, 8, 3, 0, 5);
        assertEquals(34, intSummaryStatistics.getMax());
        assertEquals(0, intSummaryStatistics.getMin());
        assertEquals(7, intSummaryStatistics.getCount());
    }

    @Test
    void testDoubleStreamSample() {
        DoubleSummaryStatistics doubleSummaryStatistics = specializedStreams.doublesSummaryStatistics(1, 6, 34, 8.5, 3, 0, 5);
        assertEquals(34, doubleSummaryStatistics.getMax());
        assertEquals(0, doubleSummaryStatistics.getMin());
        assertEquals(7, doubleSummaryStatistics.getCount());
    }
}
