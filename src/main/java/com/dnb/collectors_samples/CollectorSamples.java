package com.dnb.collectors_samples;

import com.dnb.custom_collectors.BigDecimalSummaryStatistics;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

    private static CashBalance throwIfMoreThanOneElement() {
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

    public static <T> ArrayList<T> combine(ArrayList<T> arrayList1, ArrayList<T> arrayList2) {
        arrayList1.addAll(arrayList2);
        return arrayList1;
    }

    public static <T> HashSet<T> combine(HashSet<T> hashSet1, HashSet<T> hashSet2) {
        hashSet1.addAll(hashSet2);
        return hashSet1;
    }

    public static <T> ArrayList<T> accumulate(ArrayList<T> arrayList, T t) {
        arrayList.add(t);
        return arrayList;
    }

    public static <T> HashSet<T> accumulate(HashSet<T> hashSet, T t) {
        hashSet.add(t);
        return hashSet;
    }
}
