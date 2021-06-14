package com.dnb.collectors_samples;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectorSamples {

   CashBalance collectingAndThen(List<CashBalance> cashBalanceDetails) {
        return cashBalanceDetails.stream()
                .filter(CashBalance::isOpening)
                .collect(toSingleton());
    }

    private Collector<CashBalance, Object, CashBalance> toSingleton() {
        return Collectors.collectingAndThen(Collectors.toList(), this::returnElementIfSizeOne);
    }

    private CashBalance returnElementIfSizeOne(List<CashBalance> openingCashBalances) {
        if (openingCashBalances.size() == 1) {
            return openingCashBalances.get(0);
        }
        return null;
    }

    CashBalance reduce(List<CashBalance> cashBalanceDetails) {
        return cashBalanceDetails.stream()
                .filter(CashBalance::isOpening)
                .reduce((a, b) -> throwIfMoreThanOneElement()).orElse(null);
    }

    private CashBalance throwIfMoreThanOneElement() {
        throw new MoreThanOneElementException();
    }

    static class MoreThanOneElementException extends RuntimeException {
    }
}
