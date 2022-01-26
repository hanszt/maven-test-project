package hzt.utils;

import java.util.Locale;

public final class MyStringUtils {

    private MyStringUtils() {
    }

    public static boolean containsIgnoreCase(String containingString, String containedString) {
        return containingString != null && containedString != null &&
                containingString.toUpperCase(Locale.ROOT).contains(containedString.toUpperCase(Locale.ROOT));
    }
}
