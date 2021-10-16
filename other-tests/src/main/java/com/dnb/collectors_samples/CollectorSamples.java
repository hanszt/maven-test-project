package com.dnb.collectors_samples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collector;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toUnmodifiableList;

public class CollectorSamples {

    Optional<CashBalance> collectingAndThenToFirstElementIfSizeOne(List<CashBalance> cashBalanceDetails) {
        return cashBalanceDetails.stream()
                .filter(CashBalance::isOpening)
                .collect(toFirstElementIfSizeOne());
    }

    private static <T> Collector<T, ?, Optional<T>> toFirstElementIfSizeOne() {
        return collectingAndThen(toUnmodifiableList(), CollectorSamples::returnElementIfSizeOne);
    }

    private static <T> Optional<T> returnElementIfSizeOne(List<T> list) {
        if (list.size() == 1) {
            return Optional.of(list.get(0));
        }
        return Optional.empty();
    }

    <T> T reduce(List<T> list, Predicate<T> predicate) {
        return list.stream()
                .filter(predicate)
                .reduce((a, b) -> throwIfMoreThanOneElement()).orElse(null);
    }

    private static <T> T throwIfMoreThanOneElement() {
        throw new MoreThanOneElementException();
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
