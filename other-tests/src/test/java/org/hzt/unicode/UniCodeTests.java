package org.hzt.unicode;

import org.hzt.utils.It;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unicode's encode graphemes instead of characters
 * <div>
 *     <a href="https://www.youtube.com/watch?v=ut74oHojxqo">
 *     Unicode, in friendly terms: ASCII, UTF-8, code points, character encodings, and more</a>
 * </div>
 * <div>
 *     <a href="https://www.youtube.com/watch?v=MijmeoH9LT4">
 *     Characters, Symbols and the Unicode Miracle - Computerphile</a>
 * </div>
 */
class UniCodeTests {

    private static final String G_KEY = "\uD834\uDD1E";
    private static final String SMILEY_FACE = "\uD83D\uDE00";

    /**
     * Because a char is encoded in 16 bits, only 2^16 = 65.536 different characters can be encoded.
     * <p>
     * Now there are more than a million unicode characters. For compatibility reasons,
     * surrogate pairs (two char variables) where introduced in java to accommodate all those characters.
     * Using surrogate pairs, there are 2^32 = 4.294.967.296 encodings available.
     *
     * @see java.lang.Character#isLowSurrogate(char)
     * @see java.lang.Character#isHighSurrogate(char)
     * @see <a href="https://www.informit.com/articles/article.aspx?p=2274038&seqNum=10">Surrogate Characters</a>
     * @see <a href="http://russellcottrell.com/greek/utilities/SurrogatePairCalculator.htm">The Surrogate Pair Calculator</a>
     */
    @Test
    void testSmileyFaceStringLength() {
        String smileyFace = "😀";
        String smileyFaceAsSurrogatePair = "\uD83D\uDE00";

        smileyFace.codePoints()
                .mapToObj(Integer::toHexString)
                .forEach(It::println);

        final var low = '\ude00';
        final var high = (char) 0xd83d;

        assertAll(
                () -> assertTrue(Character.isHighSurrogate(high)),
                () -> assertTrue(Character.isLowSurrogate(low)),
                () -> assertTrue(Character.isSurrogatePair(high, low)),
                () -> assertEquals(smileyFace, smileyFaceAsSurrogatePair),
                () -> assertEquals(2, smileyFace.length()),
                () -> assertEquals(1, smileyFace.codePoints().count())
        );
    }

    @Test
    void testFromCodePointToLowAndHighSurrogate() {
        final var codePoint = 0x1f600;

        final var lowSurrogate = Character.lowSurrogate(codePoint);
        final var highSurrogate = Character.highSurrogate(codePoint);
        final var surrogates = new int[]{highSurrogate, lowSurrogate};
        final var smileyFace = new String(surrogates, 0, surrogates.length);

        println(smileyFace);

        assertAll(
                () -> assertTrue(Character.isSupplementaryCodePoint(codePoint)),
                () -> assertTrue(Character.isLowSurrogate(lowSurrogate)),
                () -> assertTrue(Character.isHighSurrogate(highSurrogate)),
                () -> assertEquals(2, smileyFace.length()),
                () -> assertEquals(1, smileyFace.codePoints().count())
        );
    }

    /**
     * <a href="https://stackoverflow.com/questions/53194987/how-do-i-insert-an-emoji-in-a-java-string">
     * How do I insert an emoji in a Java string?</a>
     */
    @Test
    void testAlienEmojiString() {
        // 0x at the start of the integers means the rest of the integer is interpreted as hexadecimal
        int[] hexSurrogates = {0xD83D, 0xDC7D};
        final var alienEmoji = "\uD83D\uDC7D";

        Arrays.stream(hexSurrogates).forEach(It::println);
        Arrays.stream(hexSurrogates).mapToObj(Integer::toBinaryString).forEach(It::println);

        int[] surrogates = {55357, 56445};
        int[] binarySurrogates = {0b1101100000111101, 0b1101110001111101};

        String alienEmojiFromBinarySurrogates = new String(binarySurrogates, 0, binarySurrogates.length);
        String alienEmojiStringFromHexSurrogates = new String(hexSurrogates, 0, hexSurrogates.length);
        String alienEmojiStringFromDecimalSurrogates = new String(surrogates, 0, surrogates.length);

        println("alienEmoji = " + alienEmoji);

        assertAll(
                () -> assertEquals(alienEmoji, alienEmojiStringFromDecimalSurrogates),
                () -> assertEquals(alienEmoji, alienEmojiStringFromHexSurrogates),
                () -> assertEquals(alienEmoji, alienEmojiFromBinarySurrogates),
                () -> assertEquals(2, alienEmoji.length())
        );
    }

    @Test
    void testGKeyAsString() {
        int[] surrogates = {0xD834, 0xDD1E};
        String gKeyFromHexadecimals = new String(surrogates, 0, surrogates.length);
        String gKey = "\uD834\uDD1E";

        println("gKey = " + gKey);

        assertAll(
                () -> assertEquals(gKey, gKeyFromHexadecimals),
                () -> assertEquals(2, gKey.length())
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"anna", "lol", "racecar", "lepel", G_KEY + "lol" + G_KEY, SMILEY_FACE + "lol" + SMILEY_FACE})
    void testIsPalindrome(String s) {
        println(s);

        assertAll(
                () -> assertTrue(isPalindrome(s)),
                () -> assertTrue(isPalindromeNaive(s))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"hallo", "test", "racecars", "racekar", G_KEY + "lol" + SMILEY_FACE})
    void testIsNotAPalindrome(String s) {

        assertAll(
                () -> assertFalse(isPalindrome(s)),
                () -> assertFalse(isPalindromeNaive(s))
        );
    }

    private boolean isPalindromeNaive(String s) {
        return new StringBuilder(s).reverse().toString().equals(s);
    }

    /**
     * @param s the string to check if it is a palindrome
     * @return if the input string is a palindrome
     * @see <a href="https://www.youtube.com/watch?v=yrAfTClpNU0">
     *     Java chars are not characters, but UTF-16 code units. Palindromes with surrogate pairs are fun!</a>
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/lang/Character.html#unicode">
     *     Class Character</a>
     */
    private boolean isPalindrome(String s) {
        for (int i = 0, j = s.length() - 1; i < j; ++i, --j) {
            if (areNotEqualChars(s, i, j)) {
                return false;
            }
        }
        return true;
    }

    private boolean areNotEqualChars(String s, int i, int j) {
        final var c1 = s.charAt(i);
        return c1 != s.charAt(Character.isHighSurrogate(c1) ? j - 1 : Character.isLowSurrogate(c1) ? j + 1 : j);
    }
}
