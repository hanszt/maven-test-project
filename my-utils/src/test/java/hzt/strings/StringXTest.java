package hzt.strings;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringXTest {


    @Test
    void testToStringX() {
        final var hallo = "hallo";

        final var expected = hallo.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity()))
                .values().stream()
                .map(List::size)
                .toList();

        final var characterCounts = StringX.of(hallo).group().valuesToListXOf(List::size).toList();

        System.out.println("hallo = " + characterCounts);

        assertEquals(expected, characterCounts);
    }
}
