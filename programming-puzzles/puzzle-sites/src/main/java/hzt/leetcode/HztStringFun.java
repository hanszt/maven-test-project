package hzt.leetcode;

import org.hzt.utils.PreConditions;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.stream.IntStream;

public final class HztStringFun {

    private HztStringFun() {
    }

    /**
     * This function takes a string containing alphabetic lowercase chars between a and z
     * and increments in the same way a nr is incremented.
     * <p>
     * Works for all standard Alphabetic lowercase strings.
     * <p>
     * Function not *designed for string containing numbers and Capitals
     *
     * @param string the string to increment
     * @return the incremented alphabetic string
     * @throws IllegalArgumentException if the provided string contains characters other than alphabetic and lowercase
     */
    public static String incrementLowerCaseAlphabeticString(final String string) {
        checkRequirements(string);
        if (string.isEmpty()) {
            return "a";
        }
        if (string.length() == 1) {
            return "z".equals(string) ? "aa" : String.valueOf((char) (string.charAt(0) + 1));
        }
        final var lastIndex = string.length() - 1;
        final var substring = string.substring(0, lastIndex);
        if (string.charAt(lastIndex) != 'z') {
            return substring + (char) (string.charAt(lastIndex) + 1);
        }
        return incrementLowerCaseAlphabeticString(substring) + 'a';
    }

    public static String decrementLowerCaseAlphabeticString(final String string) {
        checkRequirements(string);
        final var length = string.length();
        final var lastIndex = length - 1;
        if ("a".repeat(length).equals(string)) {
            return "z".repeat(lastIndex);
        }
        final var substring = string.substring(0, lastIndex);
        if (string.charAt(lastIndex) != 'a') {
            return substring + (char) (string.charAt(lastIndex) - 1);
        }
        return decrementLowerCaseAlphabeticString(substring) + 'z';
    }

    private static void checkRequirements(final String string) {
        Objects.requireNonNull(string, "String must not be null");

        final IntPredicate isAlphabetic = Character::isAlphabetic;

        final var allAlphabeticLowerCase = string.codePoints().allMatch(isAlphabetic.and(Character::isLowerCase));

        PreConditions.require(allAlphabeticLowerCase, () -> "String: '" + string + "' must be alphabetic and lowercase.");
    }

    /**
     * Example
     * <p>
     * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this:
     * <p>
     * (you may want to display this pattern in a fixed font for better legibility)
     * <p>
     * P   A   H   N
     * A P L S I I G
     * Y   I   R
     * And then read line by line: "PAHNAPLSIIGYIR"
     *
     * @param input    the string ot convert
     * @param nrOfRows the nr of rows
     * @return the zigzag converted string
     * @see <a href="https://leetcode.com/problems/zigzag-conversion/">6. Zigzag Conversion</a>
     *
     * Accepted 72 ms	72.1 MB
     */
    public static String zigzagConversionMySolution(final String input, final int nrOfRows) {
        if (nrOfRows == 1 || nrOfRows >= input.length()) {
            return input;
        }
        final var inputLength = input.length();
        final var grid = new char[nrOfRows][inputLength];
        boolean goingDown = true;
        int rowi = 0;
        int coli = 0;
        for (int i = 0; i < inputLength; i++) {
            grid[rowi][coli] = input.charAt(i);
            if (rowi == 0 || rowi == nrOfRows - 1) {
                goingDown = !goingDown;
            }
            if (goingDown) {
                coli++;
            }
            rowi += goingDown ? -1 : 1;
        }
        return gridToString(grid);
    }

    @NotNull
    private static String gridToString(final char[][] grid) {
        final var sb = new StringBuilder();
        for (final char[] row : grid) {
            for (final char c : row) {
                if (c != 0) {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }

    public static String reverseZigzagConversion(final String s, final int numRows) {
        if (numRows == 1 || numRows >= s.length()) {
            return s;
        }
        final var rows = zigZagConversion(numRows, s);
        int beginIndex = 0;
        for (int i = 0; i < rows.length; i++) {
            final int endIndex = beginIndex + rows[i].length();
            rows[i] = new StringBuilder().append(s, beginIndex, endIndex);
            beginIndex = endIndex;
        }
        return constructOriginal(rows, s);
    }

    private static String constructOriginal(final StringBuilder[] rows, final String s) {
        final var result = new StringBuilder();
        int curRow = 0;
        boolean goingDown = false;
        for (int i = 0; i < s.length(); i++) {
            final var row = rows[curRow];
            result.append(row.charAt(0));
            row.deleteCharAt(0);
            if (curRow == 0 || curRow == rows.length - 1) {
                goingDown = !goingDown;
            }
            curRow += goingDown ? 1 : -1;
        }
        return result.toString();
    }

    /**
     *Accepted	5 ms	42.7 MB
     */
    public static String zigZagConversionMostVotedSolution(final String s, final int numRows) {
        if (numRows == 1 || numRows >= s.length()) {
            return s;
        }
        final var rows = zigZagConversion(numRows, s);
        final var result = new StringBuilder();
        for (final StringBuilder row : rows) {
            result.append(row);
        }
        return result.toString();
    }

    private static StringBuilder[] zigZagConversion(final int numRows, final String s) {
        final var rows = new StringBuilder[numRows];
        Arrays.setAll(rows, i -> new StringBuilder());
        int curRow = 0;
        boolean goingDown = false;
        for (final char c : s.toCharArray()) {
            rows[curRow].append(c);
            if (curRow == 0 || curRow == rows.length - 1) {
                goingDown = !goingDown;
            }
            curRow += goingDown ? 1 : -1;
        }
        return rows;
    }

    /**
     * 115. Distinct Subsequences
     *
     * @see <a href="">https://leetcode.com/problems/distinct-subsequences/</a>
     * <p>
     * // Cases
     * // s = "babgag" t = pbag =>
     * // s = "", t = ""
     * // s = "pinkpinkpink" t = "pipnk"
     * Idea
     * // countAllSubsequences from 'startS' index,  'startT'
     * *
     * 0,0
     * 1,1
     * 2,2
     */
    public static int numDistinct(String s, String t) {
        final var cache = new int[s.length()][t.length()];
        for (var ints : cache) {
            Arrays.fill(ints, -1);
        }
        return IntStream.range(0, s.length())
                .map(start -> numDistinct(s, t, start, 0, cache))
                .sum();
    }

    private static int numDistinct(String s, String t, int startS, int startT, int[][] cache) {
        if (startS >= s.length() || startT >= t.length() || s.charAt(startS) != t.charAt(startT)) {
            return 0;
        }
        if (startT == t.length() - 1) {
            return 1;
        }
        if (cache[startS][startT] != -1) {
            return cache[startS][startT];
        }
        int count = 0;
        for (int start = startS + 1; start < s.length(); ++start) {
            count += numDistinct(s, t, start, startT + 1, cache);
        }
        cache[startS][startT] = count;
        return count;
    }
}
