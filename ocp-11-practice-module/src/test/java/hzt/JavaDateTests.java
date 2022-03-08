package hzt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JavaDateTests {

    @BeforeEach
    void setup() {
        Locale.setDefault(Locale.GERMANY);
    }

    @Test
    void testFormatDate() {
        Date date = Date.from(LocalDate.of(2021, 11, 26)
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());

        assertEquals("11", new SimpleDateFormat("M").format(date));
        assertEquals("11", new SimpleDateFormat("MM").format(date));
        assertEquals("Nov", new SimpleDateFormat("MMM").format(date));
        assertEquals("novembre", new SimpleDateFormat("MMMM", Locale.FRANCE).format(date));
    }
}
