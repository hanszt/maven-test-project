package demo.sequences;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MySequenceTest {

    @Test
    @DisplayName("Test find first string with length greater than 4")
    void testFindFirstStringLengthGreaterThan4() {
        final var list = List.of("This", "is", "an", "awesome", "test", "for", "my", "sequences", "demo");

        final var expected = getFirstStringLengthGreaterThan4(list);

        println("By sequence");
        final var actual = MySequence.of(list)
                .map(this::toLength)
                .filter(this::isGreaterThan4)
                .first();

        System.out.println(actual);

        assertEquals(expected, actual);
    }

    public Integer getFirstStringLengthGreaterThan4(Iterable<String> iterable) {
        println("Imperative");
        for (String s : iterable) {
            final var length = toLength(s);
            if (isGreaterThan4(length)) {
                return length;
            }
        }
        throw new NoSuchElementException();
    }

    @Test
    @DisplayName("Test get string lengths greater than 4")
    void testStringLengthsGreaterThan4ToList() {
        final var list = List.of("This", "is", "sequence", "a", "test", "for", "my", "demo");

        final var expected = getStringLengthsGreaterThan4(list);

        println("By sequence");
        final var actual = MySequence.of(list)
                .map(this::toLength)
                .filter(this::isGreaterThan4)
                .toList();

        System.out.println(actual);

        assertEquals(expected, actual);
    }

    public List<Integer> getStringLengthsGreaterThan4(Iterable<String> iterable) {
        println("Imperative");
        List<Integer> list = new ArrayList<>();
        for (String s : iterable) {
            final var length = toLength(s);
            if (isGreaterThan4(length)) {
                list.add(length);
            }
        }
        return List.copyOf(list);
    }

    private int toLength(String string) {
        println("Getting length of '" + string + "'");
        return string.length();
    }

    private boolean isGreaterThan4(int length) {
        println("checking length '" + length + "'");
        return length > 4;
    }

}
