package com.dnb.custom_collectors;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalSummaryStatistics {

    private final long count;
    private final BigDecimal sum;
    private final BigDecimal min;
    private final BigDecimal max;

    public BigDecimalSummaryStatistics(long count, BigDecimal sum, BigDecimal min, BigDecimal max) {
        this.count = count;
        this.sum = sum;
        this.min = min;
        this.max = max;
    }

    public BigDecimal getAverage() {
        final var SCALE = 2;
        return getAverage(SCALE);
    }

    public BigDecimal getAverage(int scale) {
        return getAverage(scale, RoundingMode.HALF_UP);
    }

    public BigDecimal getAverage(int scale, RoundingMode roundingMode) {
        BigDecimal countAsBD = BigDecimal.valueOf(this.count);
        return BigDecimal.ZERO.compareTo(countAsBD) == 0 ? BigDecimal.ZERO : sum.divide(countAsBD, scale, roundingMode);
    }

    public long getCount() {
        return count;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getMax() {
        return max;
    }
}
