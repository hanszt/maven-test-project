package org.hzt;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class Strings {

    private Strings() {
    }

    public static List<String> splitNonRegex(String input, String delim) {
        if (!delim.isEmpty()) {
            return splitNonRegexNonEmptyDelim(input, delim);
        }
        return input.codePoints().mapToObj(Character::toString).toList();
    }

    @NotNull
    private static List<String> splitNonRegexNonEmptyDelim(String input, String delim) {
        List<String> l = new ArrayList<>();
        int offset = 0;
        while (true) {
            int index = input.indexOf(delim, offset);
            if (index == -1) {
                l.add(input.substring(offset));
                return l;
            }
            l.add(input.substring(offset, index));
            offset = index + delim.length();
        }
    }
}
