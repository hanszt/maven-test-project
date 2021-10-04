package hzt.stream.collectors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.Consumer;

public class BigDecimalSummaryStatistics implements Consumer<BigDecimal> {

    private long count;
    private BigDecimal sum = BigDecimal.ZERO;
    private BigDecimal min = BigDecimal.valueOf(Double.MAX_VALUE);
    private BigDecimal max = BigDecimal.valueOf(-Double.MAX_VALUE);

    public BigDecimalSummaryStatistics() {
    }

    public BigDecimalSummaryStatistics(long count, BigDecimal sum, BigDecimal min, BigDecimal max) {
        if (count < 0L) {
            throw new IllegalArgumentException("Negative count value");
        }
        if (min.compareTo(max) > 0) {
            throw new IllegalArgumentException("Minimum greater than maximum");
        }
        this.count = count;
        this.sum = sum;
        this.min = min;
        this.max = max;
    }

    @Override
    public void accept(BigDecimal value) {
        ++count;
        sum = sum.add(value);
        min = min.compareTo(value) < 0 ? min : value;
        max = max.compareTo(value) > 0 ? max : value;
    }

    public void combine(BigDecimalSummaryStatistics other) {
        count += other.count;
        sum = sum.add(other.sum);
        min = min.compareTo(other.min) < 0 ? min : other.min;
        max = max.compareTo(other.max) > 0 ? max : other.max;
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

    @Override
    public String toString() {
        return "BigDecimalSummaryStatistics[" +
                "count=" + count + ", " +
                "sum=" + sum + ", " +
                "average=" + getAverage() + ", " +
                "min=" + min + ", " +
                "max=" + max + ']';
    }
}
