package hzt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalQuery;
import java.util.Locale;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.*;

class Java8PlusDateTimeApiTests {

    private static final Locale defaultLocale = Locale.getDefault();

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.GERMANY);
    }

    @Test
    void testFormatLocalDate() {
        final var localDate = LocalDate.of(2021, 11, 26);

        assertAll(
                () -> assertEquals("11", localDate.format(ofPattern("M"))),
                () -> assertEquals("11", localDate.format(ofPattern("MM"))),
                () -> assertEquals("Nov.", localDate.format(ofPattern("MMM"))),
                () -> assertEquals("novembre", localDate.format(ofPattern("MMMM", Locale.FRANCE)))
        );
    }

    @Test
    void testIsoFormant() {
        final var fixedInstant = ZonedDateTime.parse("2011-12-03T10:15:30+01:00[Europe/Paris]").toInstant();
        var localDate = LocalDate.now(Clock.fixed(fixedInstant, ZoneId.systemDefault()));
        final var isoDateformat = localDate.format(DateTimeFormatter.ISO_DATE);

        out.println(isoDateformat);

        assertEquals("2011-12-03", isoDateformat);
    }

    @Test
    void testQueryTemporalAccessor() {
        TemporalQuery<LocalDate> localDateFrom = LocalDate::from;

        final var localDate = LocalDateTime
                .of(2022, Month.APRIL, 3, 2, 4, 3)
                .query(localDateFrom);

        assertEquals(LocalDate.of(2022, Month.APRIL, 3), localDate);
    }

    @Test
    void testJapaneseDateFromLocalDate() {
        final var now = JapaneseDate.from(LocalDate.of(2020, Month.APRIL, 2));
        out.println("now = " + now);
        assertEquals("Japanese Reiwa 2-04-02", now.toString());
    }

    /**
     * @param japaneseDate the input of the parameterized test
     * @param expected the expected result
     * @see <a href="https://stackoverflow.com/questions/57174739/how-to-parse-japanese-era-date-string-values-into-localdate-localdatetime">
     * How to parse 🎌 Japanese Era Date string values into LocalDate & LocalDateTime</a>
     */
    @ParameterizedTest(name = "Japanese date {0} converts to {1}")
    @MethodSource("argumentsLocalDatesFromJapaneseDates")
    void testLocalDatesFromJapaneseDates(String japaneseDate, LocalDate expected) {

        final var japaneseEraDtf = DateTimeFormatter.ofPattern("GGGGy年M月d日")
                .withChronology(JapaneseChronology.INSTANCE)
                .withLocale(Locale.JAPAN);

        final var localDate = LocalDate.parse(japaneseDate, japaneseEraDtf);

        assertEquals(expected, localDate);
    }

    private static Stream<Arguments> argumentsLocalDatesFromJapaneseDates() {
        return Stream.of(
                arguments("明治23年11月29日", "1890-11-29"),
                arguments("昭和22年5月3日", "1947-05-03"),
                arguments("令和5年1月11日", "2023-01-11")
        );
    }

    /**
     * @param localDate the date to test if it is a leap year
     * @see <a href="https://www.baeldung.com/parameterized-tests-junit-5">Implicit Conversion</a>
     */
    @ParameterizedTest
    @ValueSource(strings = {"2022-10-03", "1983-01-03", "1989-10-18", "1994-10-20"})
    void testParameterImplicitlyParsedToLocalDate(LocalDate localDate) {
        assertFalse(localDate.isLeapYear());
    }

    @AfterAll
    static void tearDown() {
        Locale.setDefault(defaultLocale);
    }
}
