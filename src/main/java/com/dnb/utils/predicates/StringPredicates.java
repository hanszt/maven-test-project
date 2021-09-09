package com.dnb.utils.predicates;

import java.util.function.Function;
import java.util.function.Predicate;

public final class StringPredicates {

    private StringPredicates() {
    }

    public static Predicate<String> hasEqualLength(int length) {
        return s -> s.length() == length;
    }

    public static Predicate<String> startsWith(String string) {
        return startsWith(Function.identity(), string);
    }

    public static <T> Predicate<T> startsWith(Function<T, String> toStringMapper, String string) {
        return t -> toStringMapper.apply(t).startsWith(string);
    }

    public static Predicate<String> endsWith(String other) {
        return string -> string.endsWith(other);
    }

    public static Predicate<String> contains(String string) {
        return contains(string, Function.identity());
    }

    public static <T> Predicate<T> contains(String string, Function<T, String> toStringMapper) {
        return t -> toStringMapper.apply(t).contains(string);
    }

    public static Predicate<String> doesntContain(String other) {
        return string -> !string.contains(other);
    }

    public static <T> Predicate<T> doesntContain(String other, Function<T, String> toStringMapper) {
        return t -> !toStringMapper.apply(t).contains(other);
    }

    public static Predicate<String> contentEquals(CharSequence charSequence) {
        return string -> string.contentEquals(charSequence);
    }

    public static Predicate<String> equalsIgnoreCase(String other) {
        return string -> string.equalsIgnoreCase(other);
    }

    public static Predicate<String> matches(String other) {
        return string -> string.matches(other);
    }

}
