package hzt.stream.collectors;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record BigDecimalSummaryStatistics(long count, BigDecimal sum, BigDecimal min, BigDecimal max) {

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
}
