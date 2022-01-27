package hzt.stream.collectors;

import java.util.DoubleSummaryStatistics;

public final class DoubleStatistics extends DoubleSummaryStatistics {

    private double sumOfSquare = 0.0D;
    private double sumOfSquareCompensation; // Low order bits of sum
    private double simpleSumOfSquare; // Used to compute right sum for non-finite inputs

    public DoubleStatistics() {
        super();
    }

    @Override
    public void accept(double value) {
        super.accept(value);
        double squareValue = value * value;
        simpleSumOfSquare += squareValue;
        sumOfSquareWithCompensation(squareValue);
    }

    DoubleStatistics combine(DoubleStatistics other) {
        super.combine(other);
        simpleSumOfSquare += other.simpleSumOfSquare;
        sumOfSquareWithCompensation(other.sumOfSquare);
        sumOfSquareWithCompensation(other.sumOfSquareCompensation);
        return this;
    }

    private void sumOfSquareWithCompensation(double value) {
        double tmp = value - sumOfSquareCompensation;
        double velvel = sumOfSquare + tmp; // Little wolf of rounding error
        sumOfSquareCompensation = (velvel - sumOfSquare) - tmp;
        sumOfSquare = velvel;
    }

    public double getSumOfSquare() {
        double correctedSumOfSquare =  sumOfSquare + sumOfSquareCompensation;
        if (Double.isNaN(correctedSumOfSquare) && Double.isInfinite(simpleSumOfSquare)) {
            return simpleSumOfSquare;
        }
        return correctedSumOfSquare;
    }

    public DoubleStatistics getDoubleStatistics() {
        return this;
    }

    public double getStandardDeviation() {
        final double average = getAverage();
        return getCount() > 0 ? Math.sqrt((getSumOfSquare() / getCount()) - average * average) : 0.0D;
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
