package hzt;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public final class IntegerToFullString {

    private static final Pattern pattern = Pattern.compile(", ");

    private IntegerToFullString() {
    }

    static String convertToFullString(final int integer) {
        final var inputFileName = "input/intToFullString/english_int_to_full_string.txt";
        final Map<Integer, String> integerNameMap = loadConversionTable(inputFileName);
        if (integer <= 20) {
            return integerNameMap.getOrDefault(integer, errorMessage(integer, inputFileName));
        }
        if (integer < 100) {
            return getNrNameUpToAHundred(integer, inputFileName, integerNameMap);
        }
        if (integer < 1_000) {
            return getNrNameUpToAThousand(integer, inputFileName, integerNameMap);
        }
        return errorMessage(integer, inputFileName);
    }

    private static String getNrNameUpToAThousand(final int integer, final String inputFileName, final Map<Integer, String> integerNameMap) {
        final var integerAsString = String.valueOf(integer);
        return  integerNameMap
                .getOrDefault(Integer.parseInt(integerAsString.charAt(0) + "00"), errorMessage(integer, inputFileName));
    }

    private static String getNrNameUpToAHundred(final int integer, final String inputFileName, final Map<Integer, String> integerNameMap) {
        final var integerAsString = String.valueOf(integer);
        final var endsWithZero = integerAsString.endsWith("0");
        if (endsWithZero) {
            return integerNameMap.getOrDefault(integer, errorMessage(integer, inputFileName));
        }
        final var decimal = integerNameMap
                .getOrDefault(Integer.parseInt(integerAsString.charAt(0) + "0"), errorMessage(integer, inputFileName));
        final var unit = integerNameMap
                .getOrDefault(Integer.parseInt(integerAsString.substring(1)), errorMessage(integer, inputFileName));
        return decimal + " " + unit;
    }

    @NotNull
    private static String errorMessage(final int integer, final String inputFileName) {
        return "Could not convert " + integer + " using " + inputFileName;
    }

    private static Map<Integer, String> loadConversionTable(final String inputFileName) {
        try (final var lines = Files.lines(Path.of(inputFileName))) {
            return lines
                    .filter(not(String::isBlank))
                    .map(String::trim)
                    .map(IntegerToFullString::toEntry)
                    .collect(toUnModifiableMap());
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }
    @NotNull
    private static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> toUnModifiableMap() {
        return Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    private static Map.Entry<Integer, String> toEntry(final String s) {
        final var split = pattern.split(s);
        return Map.entry(Integer.parseInt(split[0].trim()), split[1]);
    }


}
