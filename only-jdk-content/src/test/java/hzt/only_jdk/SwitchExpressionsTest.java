package hzt.only_jdk;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SwitchExpressionsTest {

    @ParameterizedTest
    @ValueSource(strings = {"a", "w", "epoch", "print", "w", "hallo"})
    void testSwitchExpression(String string) {
        LocalDate localDate = switch (string) {
            case "a" -> LocalDate.of(1980, 2, 3);
            case "w", "rt" -> LocalDate.now();
            case "epoch" -> LocalDate.EPOCH;
            case "print" -> printStringAndReturnFirstOfJanuary2000();
            default -> LocalDate.EPOCH.plusYears(5);
        };
        System.out.println(localDate);
        assertTrue(localDate.isAfter(LocalDate.EPOCH.minusDays(1)));
    }

    private static LocalDate printStringAndReturnFirstOfJanuary2000() {
        System.out.println("print");
        return LocalDate.of(2000,1,1);
    }
}
