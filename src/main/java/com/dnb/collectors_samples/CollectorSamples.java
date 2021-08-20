package com.dnb.collectors_samples;

import com.dnb.custom_collectors.BigDecimalSummaryStatistics;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.dnb.custom_collectors.BigDecimalCollectors.toBigDecimalAverage;
import static com.dnb.custom_collectors.BigDecimalCollectors.toBigDecimalSummaryStatistics;

public class CollectorSamples {

   Optional<CashBalance> collectingAndThenToFirstElementIfSizeOne(List<CashBalance> cashBalanceDetails) {
        return cashBalanceDetails.stream()
                .filter(CashBalance::isOpening)
                .collect(toFirstElementIfSizeOne());
    }

    private static Collector<CashBalance, ?, Optional<CashBalance>> toFirstElementIfSizeOne() {
        return Collectors.collectingAndThen(Collectors.toList(), CollectorSamples::returnElementIfSizeOne);
    }

    private static Optional<CashBalance> returnElementIfSizeOne(List<CashBalance> openingCashBalances) {
        if (openingCashBalances.size() == 1) {
            return Optional.of(openingCashBalances.get(0));
        }
        return Optional.empty();
    }

    CashBalance reduce(List<CashBalance> cashBalanceDetails) {
        return cashBalanceDetails.stream()
                .filter(CashBalance::isOpening)
                .reduce((a, b) -> throwIfMoreThanOneElement()).orElse(null);
    }

    private CashBalance throwIfMoreThanOneElement() {
        throw new MoreThanOneElementException();
    }

    static BigDecimal getBigDecimalAverage(Collection<BigDecimal> bigDecimals) {
       return bigDecimals.stream().collect(toBigDecimalAverage());
    }

    static BigDecimalSummaryStatistics getBigDecimalSummaryStatistics(Collection<BigDecimal> bigDecimals) {
        return bigDecimals.stream().collect(toBigDecimalSummaryStatistics());
    }

    static IntSummaryStatistics getInstSummaryStatistics(Collection<Integer> integers) {
       return integers.stream().mapToInt(i -> i).summaryStatistics();
    }

    static class MoreThanOneElementException extends RuntimeException {
    }
}
