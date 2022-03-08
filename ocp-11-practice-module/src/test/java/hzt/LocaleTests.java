package hzt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LocaleTests {

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.US);
    }
    //q?? test??
    @Test
    void testSetDefaultLocale() {
        Locale.setDefault(Locale.Category.FORMAT, Locale.US);
        Locale.setDefault(Locale.US);
        assertEquals(Locale.US, Locale.getDefault());
    }

    //q22 test 5
//    Assume that dt refers to a valid java.util.Date object and that df is a reference variable of class DateFormat.
//    Which of the following code fragments will print the country and the date in the correct local format?
    @Test
    void testPrintCountryAndDateInCorrectFormat() {
        final var calendar = Calendar.getInstance();
        calendar.set(2021, Calendar.FEBRUARY, 26);
        Date date = calendar.getTime();

        assertEquals("US", Locale.getDefault().getCountry());
        assertEquals("Feb 26, 2021", DateFormat.getDateInstance().format(date));
    }

    @Test
    void testAvailableLocales() {
        final var availableLocales = Stream.of(NumberFormat.getAvailableLocales())
                .map(Locale::toString)
                .collect(Collectors.toUnmodifiableSet());

        availableLocales.forEach(System.out::println);
        assertTrue(availableLocales.containsAll(Set.of("nl_NL", "nl_BQ", "nl_BE", "nl_AW", "nl_SX")));
    }

    @AfterAll
    static void setLocaleToGermany() {
        Locale.setDefault(Locale.GERMANY);
    }
}
