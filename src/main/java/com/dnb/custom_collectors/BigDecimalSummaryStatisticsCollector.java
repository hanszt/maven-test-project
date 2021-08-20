package com.dnb.custom_collectors;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

class BigDecimalSummaryStatisticsCollector implements
        Collector<BigDecimal, BigDecimalSummaryStatisticsCollector.BigDecimalAccumulator, BigDecimalSummaryStatistics> {

    BigDecimalSummaryStatisticsCollector() {
    }

    @Override
    public Supplier<BigDecimalAccumulator> supplier() {
        return BigDecimalAccumulator::new;
    }

    @Override
    public BiConsumer<BigDecimalAccumulator, BigDecimal> accumulator() {
        return BigDecimalAccumulator::add;
    }

    @Override
    public BinaryOperator<BigDecimalAccumulator> combiner() {
        return BigDecimalAccumulator::combine;
    }

    @Override
    public Function<BigDecimalAccumulator, BigDecimalSummaryStatistics> finisher() {
        return BigDecimalAccumulator::getSummaryStatistics;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Collections.emptySet();
    }

    static final class BigDecimalAccumulator {

        private BigDecimal min = BigDecimal.valueOf(Double.MAX_VALUE);
        private BigDecimal max = BigDecimal.valueOf(Double.MIN_VALUE);
        private BigDecimal sum;
        private BigDecimal count;

        private BigDecimalAccumulator(BigDecimal sum, BigDecimal count) {
            this.sum = sum;
            this.count = count;
        }

        private BigDecimalAccumulator() {
            this(BigDecimal.ZERO, BigDecimal.ZERO);
        }

        private BigDecimalSummaryStatistics getSummaryStatistics() {
            return new BigDecimalSummaryStatistics(count, sum, min, max);
        }

        private BigDecimalAccumulator combine(BigDecimalAccumulator other) {
            return new BigDecimalAccumulator(sum.add(other.sum), count.add(other.count));
        }

        void add(BigDecimal next) {
            min = next.compareTo(min) > 0 ? min : next;
            max = next.compareTo(max) < 0 ? max : next;
            count = count.add(BigDecimal.ONE);
            sum = sum.add(next);
        }
    }

}
