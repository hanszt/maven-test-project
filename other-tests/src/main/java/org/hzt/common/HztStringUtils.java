package org.hzt.common;

public final class HztStringUtils {

    private HztStringUtils() {
    }

    public static String toOnlyFirstLetterUpperCase(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
