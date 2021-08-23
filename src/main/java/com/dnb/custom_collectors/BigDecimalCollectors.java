package com.dnb.custom_collectors;

public final class BigDecimalCollectors {

    private BigDecimalCollectors() {
    }

    public static BigDecimalAverageCollector toBigDecimalAverage() {
        return new BigDecimalAverageCollector();
    }

    public static BigDecimalSummaryStatisticsCollector toBigDecimalSummaryStatistics() {
        return new BigDecimalSummaryStatisticsCollector();
    }
}
