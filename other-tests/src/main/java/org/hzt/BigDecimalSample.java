package org.hzt;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

public final class BigDecimalSample {

    private BigDecimalSample() {
    }

    /**
     * @see <a href="https://baeldung-cn.com/java-string-to-bigdecimal">Converting String to BigDecimal in Java</a>
     *
     * @param source The string to convert to bigdecimal
     * @param pattern The pattern to use for it
     * @return The BigDecimal value
     */
    static BigDecimal parseBigDecimal(String source, String pattern) {
        final var symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator(',');
        symbols.setDecimalSeparator('.');
        final var decimalFormat = new DecimalFormat(pattern, symbols);
        decimalFormat.setParseBigDecimal(true);
        try {
            if (decimalFormat.parse(source) instanceof BigDecimal bd) {
                return bd;
            }
            throw new IllegalStateException("Decimal format parse not of instance BigDecimal");
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
    }
}
