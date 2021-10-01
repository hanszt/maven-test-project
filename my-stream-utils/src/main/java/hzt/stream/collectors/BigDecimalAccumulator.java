package hzt.stream.collectors;

import java.math.BigDecimal;
import java.math.RoundingMode;

class BigDecimalAccumulator {

    private long count;
    private BigDecimal sum;
    private BigDecimal min;
    private BigDecimal max;

    private BigDecimalAccumulator(long count, BigDecimal sum, BigDecimal min, BigDecimal max) {
        this.sum = sum;
        this.count = count;
        this.min = min;
        this.max = max;
    }

    BigDecimalAccumulator() {
        this(0L, BigDecimal.ZERO, BigDecimal.valueOf(Double.MAX_VALUE), BigDecimal.valueOf(Double.MIN_VALUE));
    }

    BigDecimalSummaryStatistics getSummaryStatistics() {
        return new BigDecimalSummaryStatistics(count, sum, min, max);
    }

    BigDecimal getAverage() {
        final var SCALE = 2;
        BigDecimal countAsBD = BigDecimal.valueOf(this.count);
        return BigDecimal.ZERO.compareTo(countAsBD) == 0 ? BigDecimal.ZERO : sum.divide(countAsBD, SCALE, RoundingMode.HALF_UP);
    }

    BigDecimalAccumulator combine(BigDecimalAccumulator other) {
        BigDecimal combinedMin = min.compareTo(other.min) < 0 ? min : other.min;
        BigDecimal combinedMax = max.compareTo(other.max) > 0 ? max : other.max;
        return new BigDecimalAccumulator(count + other.count, sum.add(other.sum), combinedMin, combinedMax);
    }

    void accumulate(BigDecimal next) {
        count++;
        sum = sum.add(next);
        min = min.compareTo(next) < 0 ? min : next;
        max = max.compareTo(next) > 0 ? max : next;
    }
}

