package demo;

import org.hzt.utils.sequences.primitives.IntSequence;
import org.jetbrains.annotations.NotNull;

import java.time.Year;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.hzt.utils.It.println;

public class FirstLeapYear {

    public static void main(String[] args) {
        final var years = new int[]{1988, 1989, 2003, 1996, 2028, 2050, 2000, 2004};
//        final var firstLeapYear = firstLeapYearAfter2000V1(ints);
        final var firstLeapYear = firstLeapYearAfter2000ByStream(years);
//        final var firstLeapYear = firstLeapYearAfter2000BySequence(years);
        println(firstLeapYear);
    }

    static String firstLeapYearAfter2000Imperative(int... years) {
        for (int year : years) {
            final var leap = isLeap(year);
            if (leap) {
                final var yearAfter2000 = yearAfter2000(year);
                if (yearAfter2000) {
                    return yearToString(year);
                }
            }
        }
        throw new NoSuchElementException();
    }

    static String firstLeapYearAfter2000ByStream(int... years) {
        return IntStream.of(years)
                .filter(year -> isLeap(year))
                .filter(year -> yearAfter2000(year))
                .mapToObj(year -> yearToString(year))
                .findFirst()
                .orElseThrow();
    }

    static String firstLeapYearAfter2000BySequence(int... years) {
        return IntSequence.of(years)
                .filter(year -> isLeap(year))
                .filter(year -> yearAfter2000(year))
                .mapToObj(year -> yearToString(year))
                .first();
    }

    private static boolean yearAfter2000(int year) {
        final var isAfter2000 = year > 2_000;
        println(year + " is after 2000: " + isAfter2000);
        return isAfter2000;
    }

    @NotNull
    private static String yearToString(int year) {
        println(year + " to string");
        return String.valueOf(year);
    }

    private static boolean isLeap(int year) {
        final var isLeap = Year.of(year).isLeap();
        println(year + " is leap year: " + isLeap);
        return isLeap;
    }
}
