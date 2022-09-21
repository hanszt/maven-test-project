package org.hzt;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static java.util.function.Predicate.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ScannerTests {

    @Test
    void testScannerTokens() {
        final var strings = new Scanner("Dit is, een, string")
                .useDelimiter(",")
                .tokens()
                .toList();

        assertEquals(List.of("Dit is", " een", " string"), strings);
    }

    @Test
    void givenDataInSystemIn_whenCallingTokens_thenHaveUserInputData() {
        final var stopKeyWord = "bye";
        final String[] inputLines = {
                "The first line.",
                "The second line.",
                "The last line.",
                stopKeyWord,
                "anything after 'bye' will be ignored"
        };
        final var expectedLines = Arrays.copyOf(inputLines, inputLines.length - 2);
        final var expected = Arrays.stream(expectedLines).toList();

        final InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(String.join("\n", inputLines).getBytes()));

            final var actual = new Scanner(System.in)
                    .useDelimiter("\n")
                    .tokens()
                    .takeWhile(not(stopKeyWord::equalsIgnoreCase))
                    .toList();

            assertEquals(expected, actual);
        } finally {
            System.setIn(stdin);
        }
    }
}
