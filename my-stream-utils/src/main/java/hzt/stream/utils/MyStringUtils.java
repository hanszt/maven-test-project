package hzt.stream.utils;

import java.util.Locale;

public final class MyStringUtils {

    private MyStringUtils() {
    }

    public static boolean containsIgnoreCase(String containingString, String containedString) {
        if (containingString != null && containedString != null) {
            return containingString.toUpperCase(Locale.ROOT).contains(containedString.toUpperCase(Locale.ROOT));
        } else {
            return false;
        }
    }
}
