package hzt.only_jdk;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StringTests {

    @Test
    void testCodePointsMethodMapMultiComparedToChars() {
        final var testString = "This is a test, 2, 5?@";

        final var expected = testString.chars()
                .mapToObj(c -> (char) c)
                .filter(Character::isLetterOrDigit)
                .toList();

        final var characters1 = testString.codePoints()
                .filter(Character::isLetterOrDigit)
                .mapToObj(Character::toChars)
                .mapMulti(this::forEachChar)
                .toList();

        final var characters2 = testString.codePoints()
                .filter(Character::isLetterOrDigit)
                .filter(Character::isBmpCodePoint)
                .mapToObj(Character::toChars)
                .flatMap(this::toCharacterStream)
                .toList();

        System.out.println(characters1);

        assertAll(
                () -> assertEquals(expected, characters1),
                () -> assertEquals(expected, characters2)
        );
    }

    private void forEachChar(char[] chars, Consumer<Character> consumer) {
        for (char c : chars) {
            consumer.accept(c);
        }
    }

    private Stream<Character> toCharacterStream(char[] chars) {
        final Iterator<Character> iterator = new Iterator<>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < chars.length;
            }

            @Override
            public Character next() {
                return chars[index++];
            }
        };
        return StreamSupport.stream(Spliterators.spliterator(iterator, chars.length, 0), false);
    }

    @Test
    void testCodePointStreamToStringUsingStringBuilder() {
        final var text = "I have a text with 123452, and I need to convert it to a text without nrs like 3 and without special characters #$%!";

        final IntPredicate isMirrored = Character::isMirrored;

        final var expected = text.codePoints()
                .filter(isMirrored.or(Character::isLetter))
                .mapToObj(Character::toString)
                .collect(Collectors.joining());

        final var string1 = text.codePoints()
                .filter(isMirrored.or(Character::isLetter))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        System.out.println("string = " + string1);

        assertAll(
                () -> assertEquals(81, string1.length()),
                () -> assertEquals(expected, string1)
        );
    }

    @Test
    void testCodePointNames() {
        final var text = "I have a text with 123452, and I need to convert it to a text without nrs like 3 and without special characters #$%!";

        final var names = text.codePoints()
                .mapToObj(Character::getName)
                .toList();

        names.forEach(System.out::println);

        assertEquals(116, names.size());
    }

    @Test
    void testCharacterToString() {
        final var string = "This is another test text";

        final var expected = string.codePoints()
                .mapToObj(Character::toString)
                .toList();

        final var actual = string.chars()
                .mapToObj(Character::toString)
                .toList();

        actual.forEach(System.out::println);

        assertEquals(expected, actual);
    }

    @Test
    void testCodePointStreamEnglishCharacterTextEqualSizeCharStream() {
        final var testText = "The test text. 1,2,3,4,5,6,%$#";

        final var codePointCount = testText.codePoints().count();
        final var charCount = testText.chars().count();

        assertEquals(codePointCount, charCount);
    }
}
