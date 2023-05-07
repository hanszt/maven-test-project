package hzt.only_jdk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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

    @ParameterizedTest
    @ValueSource(strings = {"3445", "3764", "3543","3245", "5674"})
    void utrechtZipCodesTest(String zipCode) {
        assertEquals("Utrecht", provinceNameByZipCode(zipCode));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2311", "2313", "2314"})
    void zuidHollandZipCodesTest(String zipCode) {
        assertEquals("Zuid-Holland", provinceNameByZipCode(zipCode));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2344", "1234", "3452"})
    void noordHollandZipCodesTest(String zipCode) {
        assertEquals("Noord-Holland", provinceNameByZipCode(zipCode));
    }

    @ParameterizedTest
    @ValueSource(strings = {"4331", "4333", "4334"})
    void zeelandZipCodesTest(String zipCode) {
        assertEquals("Zeeland", provinceNameByZipCode(zipCode));
    }

    @ParameterizedTest
    @ValueSource(strings = {"453gw", "245gw", "sdffs", "sdfssf", "sdfs", "2312"})
    void throwIllegalStateExceptionWhenUnknownZipCode(String zipCode) {
        Assertions.assertThrows(IllegalStateException.class, () -> provinceNameByZipCode(zipCode));
    }

    @Test
    void brabantZipCodeTest() {
        assertEquals("Brabant", provinceNameByZipCode("5454"));
    }

    private String provinceNameByZipCode(String zipCode) {
        return switch (zipCode) {
            case "2311", "2313", "2314" -> "Zuid-Holland";
            case "2344", "1234", "3452" -> "Noord-Holland";
            case "4331", "4332", "4333", "4334" -> "Zeeland";
            case "5454" -> {
                doSomethingFirst();
                yield "Brabant";
            }
            case "3445", "3764", "3543","3245", "5674" -> "Utrecht";
            case "5343" -> "Friesland";
            default -> throw new IllegalStateException("Unexpected value: " + zipCode);
        };
    }

    private void doSomethingFirst() {
        System.out.println("This is Brabant!");
    }
}
