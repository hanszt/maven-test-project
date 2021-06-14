package com.dnb;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.System.*;

public class BigDecimalScalingExample {

    private static final String SCALE_OF = "Scale of ";
    private static final String IS = " is: ";

    public static void main(String[] args) {
        var bigDecimal = new BigDecimal("123.1267854341");
        var bigDecimal2 = new BigDecimal("1231267854341");

        var scaledBigDecimalFloor = bigDecimal.setScale(2, RoundingMode.FLOOR);
        var scaledBigDecimalCeil = bigDecimal.setScale(2, RoundingMode.CEILING);
        var scaledBigDecimalHalfDown = bigDecimal.setScale(2, RoundingMode.HALF_DOWN);
        var scaledBigDecimalHalfEven = bigDecimal.setScale(2, RoundingMode.HALF_EVEN);
        var scaledBigDecimalHalfUp = bigDecimal2.setScale(4, RoundingMode.HALF_UP);

        out.println(bigDecimal + " after changing the scale to 2 and rounding with floor is " + scaledBigDecimalFloor);
        out.println(bigDecimal + " after changing the scale to 2 and rounding with ceil is " + scaledBigDecimalCeil);
        out.println(bigDecimal + " after changing the scale to 2 and rounding with half down is " + scaledBigDecimalHalfDown);
        out.println(bigDecimal + " after changing the scale to 2 and rounding with half even is " + scaledBigDecimalHalfEven);
        out.println(bigDecimal2 + " after changing the scale to 4 and rounding with mode half up is " + scaledBigDecimalHalfUp);

        out.println(SCALE_OF + bigDecimal + IS + bigDecimal.scale());
        out.println(SCALE_OF + scaledBigDecimalHalfUp + IS + scaledBigDecimalHalfUp.scale());
        out.println(SCALE_OF + scaledBigDecimalHalfDown + IS + scaledBigDecimalHalfDown.scale());
    }
}