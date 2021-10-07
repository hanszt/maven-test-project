package hzt.stream.collectors;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class BigDecimalStatistics extends BigDecimalSummaryStatistics {

    private BigDecimal sumOfSquare = BigDecimal.ZERO;

    BigDecimalStatistics() {
        super();
    }

    @Override
    public void accept(BigDecimal value) {
        super.accept(value);
        BigDecimal squareValue = value.multiply(value);
        sumOfSquare = sumOfSquare.add(squareValue);
    }

    public BigDecimalStatistics combine(BigDecimalStatistics other) {
        super.combine(other);
        sumOfSquare = sumOfSquare.add(other.sumOfSquare);
        return this;
    }

    public BigDecimal getSumOfSquare() {
        return sumOfSquare;
    }

    public BigDecimalStatistics getStatistics() {
        return this;
    }

    public BigDecimal getStandardDeviation() {
        return getStandardDeviation(2, RoundingMode.HALF_UP, MathContext.DECIMAL32);
    }

    public BigDecimal getStandardDeviation(int scale, RoundingMode roundingMode) {
        return getStandardDeviation(scale, roundingMode, MathContext.DECIMAL128);
    }

    public BigDecimal getStandardDeviation(int scale, RoundingMode roundingMode, MathContext mathContext) {
        final BigDecimal average = getAverage(scale, roundingMode);
        return getCount() > 0 ?
                ((getSumOfSquare().divide(BigDecimal.valueOf(getCount()), scale, roundingMode))
                        .subtract(average.multiply(average))).sqrt(mathContext) : BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return String.format(
                "%s{count=%d, sum=%f, min=%f, average=%f, max=%f, standard deviation=%f}",
                this.getClass().getSimpleName(),
                getCount(),
                getSum(),
                getMin(),
                getAverage(),
                getMax(),
                getStandardDeviation());
    }

}
