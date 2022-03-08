package hzt.only_jdk;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static java.text.NumberFormat.*;
import static java.text.NumberFormat.Style.*;
import static java.util.Locale.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CompactNumberFormattingTest {

    private record Data(Number number,
                        String expectedEnglish,
                        String expectedEnglishShort,
                        String expectedFrench,
                        String expectedChinees) {
    }

    private static final List<Data> EXPECTED_DATA_LIST = List.of(
            new Data(43535L, "44 thousand", "44K", "44 mille", "4万"),
            new Data(23235235235L, "23 billion", "23B", "23 milliards", "232亿"),
            new Data(10_000_342.45, "10 million", "10M", "10 millions", "1000万"),
            new Data(676L, "676", "676", "676", "676"),
            new Data(.0004, "0", "0", "0", "0"),
            new Data(20e13, "200 trillion", "200T", "200 billion", "200万亿"),
            new Data(-4543.643, "-5 thousand", "-5K", "-5 mille", "-4,544"));

    @Test
    void testCompactNumberFormatLong() {
        EXPECTED_DATA_LIST.forEach(data -> resultEqualsExpected(ENGLISH, LONG, data.expectedEnglish, data.number));
    }

    @Test
    void testCompactNumberFormatShort() {
        EXPECTED_DATA_LIST.forEach(data -> resultEqualsExpected(ENGLISH, SHORT, data.expectedEnglishShort, data.number));
    }

    @Test
    void testCompactNumberFormatLongFrench() {
        EXPECTED_DATA_LIST.forEach(data -> resultEqualsExpected(FRENCH, LONG, data.expectedFrench, data.number));
    }

    @Test
    void testCompactNumberFormatLongChinees() {
        EXPECTED_DATA_LIST.forEach(data -> resultEqualsExpected(CHINESE, LONG, data.expectedChinees, data.number));
    }

    private void resultEqualsExpected(Locale locale, Style style, String expected, Number number) {
        var result = getCompactNumberInstance(locale, style).format(number);
        assertEquals(expected, result);
    }
}
