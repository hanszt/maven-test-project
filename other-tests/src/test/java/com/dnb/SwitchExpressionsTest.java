package com.dnb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

class SwitchExpressionsTest {

    @ParameterizedTest
    @ValueSource(strings = {"a", "w", "epoch", "print", "w", "hallo"})
    void testSwitchExpression(String string) {
        LocalDate localDate = switch (string) {
            case "a" -> LocalDate.of(1980, 2, 3);
            case "w", "rt" -> LocalDate.now();
            case "epoch" -> LocalDate.EPOCH;
            case "print" -> printString();
            default -> LocalDate.EPOCH.plusYears(5);
        };
        System.out.println(localDate);
        Assertions.assertTrue(localDate.isAfter(LocalDate.EPOCH.minusDays(1)));
    }

    private static LocalDate printString() {
        System.out.println("print");
        return LocalDate.of(2000,1,1);
    }
}
