package org.hzt;

import java.util.function.Predicate;

import static org.hzt.stream.StreamUtils.anyMatch;
import static org.hzt.stream.StreamUtils.noneMatch;

public final class StringPredicates {

    private StringPredicates() {
    }

    public static Predicate<String> endsWithAnyOf(String... strings) {
        return string -> string != null && anyMatch(string::endsWith, strings);
    }

    public static Predicate<String> endsWithNoneOf(String... strings) {
        return string -> string != null && noneMatch(string::endsWith, strings);
    }
    public static Predicate<String> startsWithAnyOf(String... strings) {
        return string -> string != null && anyMatch(string::startsWith, strings);
    }

    public static Predicate<String> startsWithNoneOf(String... strings) {
        return string -> string != null && noneMatch(string::startsWith, strings);
    }
}
