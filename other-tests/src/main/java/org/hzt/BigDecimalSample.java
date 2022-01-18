package org.hzt;

import java.math.BigDecimal;

public final class BigDecimalSample {

    private BigDecimalSample() {
    }

    static boolean comparingBigDecimalsUsingCompareTo(BigDecimal expected, BigDecimal actual) {
        return expected.compareTo(actual) == 0;
    }

    static boolean  comparingBigDecimalsUsingEquals(BigDecimal expected, BigDecimal actual) {
        return actual.equals(expected);
    }
}
