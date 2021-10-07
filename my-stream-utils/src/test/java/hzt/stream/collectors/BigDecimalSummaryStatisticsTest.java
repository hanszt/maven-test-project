package hzt.stream.collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalSummaryStatisticsTest {

    @Test
    void testThrowsIllegalArgumentExceptionWhenMinGreaterThanMax() {
        assertThrows(IllegalArgumentException.class,
                () -> new BigDecimalSummaryStatistics(0, BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ZERO));
    }

    @Test
    void testThrowsIllegalArgumentExceptionWhenCountIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> new BigDecimalSummaryStatistics(-1, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ONE));
    }

    @Test
    void testDoesNotThrow() {
        assertDoesNotThrow((ThrowingSupplier<BigDecimalSummaryStatistics>) BigDecimalSummaryStatistics::new);
    }

    @Test
    void testUsingBigDecimalSummaryStatisticsListInMapMulti() {
        final var summaryStatistics = new BigDecimalSummaryStatistics(4, BigDecimal.ONE, BigDecimal.ZERO, BigDecimal.TEN);
        final var bigDecimalSummaryStatisticsList = List.of(new BigDecimalSummaryStatistics(), summaryStatistics);

        final var averages = bigDecimalSummaryStatisticsList.stream()
                .<BigDecimal>mapMulti((statistics, consumer) -> consumer.accept(statistics.getAverage()))
                .toList();

        System.out.println(averages);

        assertFalse(averages.isEmpty());
    }
}
