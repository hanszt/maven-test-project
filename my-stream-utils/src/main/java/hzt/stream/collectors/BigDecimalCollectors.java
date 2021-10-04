package hzt.stream.collectors;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;

public final class BigDecimalCollectors {

    private static final Set<Collector.Characteristics> CH_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

    private static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

    private BigDecimalCollectors() {
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> averagingBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper, int scale, RoundingMode roundingMode) {
        return getBigDecimalCollectorImpl(toBigDecimalMapper,
                summaryStatistics -> summaryStatistics.getAverage(scale, roundingMode), CH_NOID);
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> averagingBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper) {
        return averagingBigDecimal(toBigDecimalMapper, 2, RoundingMode.HALF_UP);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimal> averagingBigDecimal() {
        return averagingBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> toMaxBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalCollectorImpl(toBigDecimalMapper, BigDecimalSummaryStatistics::getMax, CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimal> toMaxBigDecimal() {
        return toMaxBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> toMinBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalCollectorImpl(toBigDecimalMapper, BigDecimalSummaryStatistics::getMin, CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimal> toMinBigDecimal() {
        return toMinBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimalSummaryStatistics> summarizingBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalCollectorImpl(toBigDecimalMapper, Function.identity(), CH_ID);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimalSummaryStatistics> summarizingBigDecimal() {
        return summarizingBigDecimal(Function.identity());
    }

    public static <T> Collector<T, BigDecimalSummaryStatistics, BigDecimal> summingBigDecimal(
            Function<? super T, BigDecimal> toBigDecimalMapper) {
        return getBigDecimalCollectorImpl(toBigDecimalMapper, BigDecimalSummaryStatistics::getSum, CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalSummaryStatistics, BigDecimal> summingBigDecimal() {
        return summingBigDecimal(Function.identity());
    }

    private static <T, R> CollectorImpl<T, BigDecimalSummaryStatistics, R> getBigDecimalCollectorImpl(
            Function<? super T, BigDecimal> toBigDecimalMapper, Function<BigDecimalSummaryStatistics, R> finisher,
            Set<Collector.Characteristics> characteristics) {
        Objects.requireNonNull(toBigDecimalMapper);
        return new CollectorImpl<>(BigDecimalSummaryStatistics::new,
                (bigDecimalSummaryStatistics, t) -> bigDecimalSummaryStatistics.accept(toBigDecimalMapper.apply(t)),
                BigDecimalCollectors::combineBigDecimalSummaryStatistics,
                finisher,
                characteristics);
    }

    private static BigDecimalSummaryStatistics combineBigDecimalSummaryStatistics(
            BigDecimalSummaryStatistics left, BigDecimalSummaryStatistics right) {
        left.combine(right);
        return right;
    }
}
