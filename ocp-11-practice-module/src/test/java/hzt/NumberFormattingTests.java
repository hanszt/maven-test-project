package hzt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

class NumberFormattingTests {

    @BeforeEach
    void setup() {
        Locale.setDefault(Locale.US);
    }

    @Test
    void testCurrencyNumberFormattingNl() {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("nl", "NL"));
        final var formattedAsCurrency = numberFormat.format(BigDecimal.valueOf(10));

        //&nbsp No-Break Space (NBSP) unicode code is \u00a0
        // In word processing and digital typesetting, a non-breaking space,  ,
        // also called NBSP, required space,[1] hard space, or fixed space (though it is not of fixed width),
        // is a space character that prevents an automatic line break at its position. In some formats, including HTML,
        // it also prevents consecutive whitespace characters from collapsing into a single space.
        assertEquals("€\u00a010,00", formattedAsCurrency);
    }

    @Test
    void testCurrencyNumberFormattingUs() {
        assertEquals("$10.00", NumberFormat.getCurrencyInstance(Locale.US).format(BigDecimal.valueOf(10)));
    }

    @Test
    void testNumberFormatting() {
        assertEquals("10", NumberFormat.getInstance(Locale.GERMANY).format(BigDecimal.valueOf(10)));
    }

    @Test
    void testNumberFormattingPercentInstance() {
        assertEquals("10%", NumberFormat.getPercentInstance().format(.1));
    }

    @Test
    void testToIntegerRoundingDown() {
        assertEquals("3", NumberFormat.getIntegerInstance().format(3.45454));
    }

    @Test
    void testToIntegerRoundingUp() {
        assertEquals("4", NumberFormat.getIntegerInstance().format(3.5));
    }

    @Test
    void testToDouble() {
        assertThrowsExactly(IllegalArgumentException.class,() -> NumberFormat.getInstance().format("Hallo"));
    }
}
