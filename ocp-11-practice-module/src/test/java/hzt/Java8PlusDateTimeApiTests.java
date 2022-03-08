package hzt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ofPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Java8PlusDateTimeApiTests {

    @BeforeEach
    void setup() {
        Locale.setDefault(Locale.GERMANY);
    }

    @Test
    void testFormatLocalDate() {
        final var localDate = LocalDate.of(2021, 11, 26);

        assertEquals("11", localDate.format(ofPattern("M")));
        assertEquals("11", localDate.format(ofPattern("MM")));
        assertEquals("Nov.", localDate.format(ofPattern("MMM")));
        assertEquals("novembre", localDate.format(ofPattern("MMMM", Locale.FRANCE)));
    }
}
