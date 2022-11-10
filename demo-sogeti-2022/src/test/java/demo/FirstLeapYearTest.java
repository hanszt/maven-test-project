package demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FirstLeapYearTest {

    @Test
    void testGroupBy() {
        final var years = new int[]{1988, 1989, 2003, 1996, 2028, 2050, 2000, 2004};
        final var imperative = FirstLeapYear.firstLeapYearAfter2000Imperative(years);
        final var streamsResult = FirstLeapYear.firstLeapYearAfter2000ByStream(years);
        final var sequencesResult = FirstLeapYear.firstLeapYearAfter2000BySequence(years);

        assertAll(
                () -> assertEquals(streamsResult, imperative),
                () -> assertEquals(sequencesResult, streamsResult)
        );
    }

}
