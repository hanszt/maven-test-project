package hzt.stream.predicates;

import hzt.stream.utils.MyStringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static hzt.stream.StreamUtils.anyMatch;
import static hzt.stream.StreamUtils.noneMatch;

public final class StringPredicates {

    private StringPredicates() {
    }

    public static Predicate<String> hasEqualLength(int length) {
        return s -> s.length() == length;
    }

    public static Predicate<String> hasEqualLength(String other) {
        return s -> s != null && other != null && s.length() == other.length();
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
        return t -> {
            final var mappedString = toStringMapper.apply(t);
            return mappedString != null && mappedString.contains(string);
        };
    }

    public static Predicate<String> containsAllOf(String... strings) {
        return string -> Stream.of(strings).filter(Objects::nonNull).allMatch(string::contains);
    }

    public static Predicate<String> containsAllOf(Collection<String> strings) {
        return containsAllOf(strings.toArray(String[]::new));
    }

    public static Predicate<String> containsAnyOf(String... strings) {
        return string -> Stream.of(strings).filter(Objects::nonNull).anyMatch(string::contains);
    }

    public static Predicate<String> containsAnyOf(Collection<String> strings) {
        return containsAnyOf(strings.toArray(String[]::new));
    }

    public static Predicate<String> containsNoneOf(String... strings) {
        return string -> Stream.of(strings).filter(Objects::nonNull).noneMatch(string::contains);
    }

    public static Predicate<String> containsNoneOf(Collection<String> strings) {
        return containsNoneOf(strings.toArray(String[]::new));
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

    public static Predicate<String> isEqualIgnoreCase(String testString) {
        return string -> string != null && string.equalsIgnoreCase(testString);
    }

    public static Predicate<String> equalsIgnoreCase(String other) {
        return string -> string.equalsIgnoreCase(other);
    }

    public static Predicate<String> matches(String other) {
        return string -> string.matches(other);
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

    public static Predicate<String> containsIgnoreCaseAllOf(String... strings) {
        return string -> Stream.of(strings)
                .allMatch(searchString -> MyStringUtils.containsIgnoreCase(string, searchString));
    }

    public static Predicate<String> containsIgnoreCaseAnyOf(String... strings) {
        return string -> Stream.of(strings)
                .anyMatch(searchString -> MyStringUtils.containsIgnoreCase(string, searchString));
    }

    public static Predicate<String> containsIgnoreCaseNoneOf(String... strings) {
        return string -> Stream.of(strings)
                .noneMatch(searchString -> MyStringUtils.containsIgnoreCase(string, searchString));
    }

}
