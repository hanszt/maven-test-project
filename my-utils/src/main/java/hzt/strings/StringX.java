package hzt.strings;

import hzt.collections.IterableX;
import hzt.collections.MutableListX;
import hzt.function.It;
import org.jetbrains.annotations.NotNull;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class StringX implements CharSequence, IterableX<Character> {

    private final String string;

    private StringX(String string) {
        this.string = string;
    }

    private StringX(CharSequence charSequence) {
        this(String.valueOf(charSequence));
    }

    private StringX(char[] charArray) {
        this(new String(charArray));
    }

    private StringX(byte[] bytes) {
        this(bytes, StandardCharsets.UTF_8);
    }

    private StringX(byte[] bytes, Charset charset) {
        this(new String(bytes, charset));
    }

    public static StringX of(String s) {
        return new StringX(s);
    }

    public static StringX of(CharSequence s) {
        return new StringX(s);
    }

    public static StringX of(char[] s) {
        return new StringX(s);
    }

    public static StringX of(char first, char... chars) {
        char[] charsArray = new char[chars.length + 1];
        charsArray[0] = first;
        System.arraycopy(chars, 0, charsArray, 1, charsArray.length - 1);
        return new StringX(charsArray);
    }

    public static StringX of(byte[] s) {
        return new StringX(s);
    }

    public static StringX of(byte[] s, Charset charset) {
        return new StringX(s, charset);
    }

    public MutableListX<Character> toListX() {
        return toMutableList();
    }

    public MutableListX<Character> toMutableList() {
        return toMutableListOf(It::self);
    }

    public StringX replaceFirstChar(UnaryOperator<Character> replacer) {
        var charArray = toCharArray();
        if (charArray.length > 0) {
            charArray[0] = replacer.apply(charArray[0]);
        }
        return StringX.of(charArray);
    }

    @Override
    public Iterable<Character> iterable() {
        return charIterable(string);
    }

    private Iterable<Character> charIterable(String string) {
        final class CharIterable implements Iterable<Character> {

            private int index = 0;
            private final char[] charArray = string.toCharArray();
            @NotNull
            @Override
            public Iterator<Character> iterator() {
                return new Iterator<>() {

                    @Override
                    public boolean hasNext() {
                        return index < charArray.length;
                    }

                    @Override
                    public Character next() {
                        int prevIndex = index;
                        if (prevIndex >= charArray.length) {
                            throw new NoSuchElementException();
                        }
                        return charArray[index++];
                    }
                };
            }
        }
        return new CharIterable();
    }

    @Override
    public int length() {
        return string.length();
    }

    public boolean isEmpty() {
        return string.isEmpty();
    }

    @Override
    public char charAt(int index) {
        return string.charAt(index);
    }

    public int codePointAt(int index) {
        return string.codePointAt(index);
    }

    public int codePointBefore(int index) {
        return string.codePointBefore(index);
    }

    public int codePointCount(int beginIndex, int endIndex) {
        return string.codePointCount(beginIndex, endIndex);
    }

    public int offsetByCodePoints(int index, int codePointOffset) {
        return string.offsetByCodePoints(index, codePointOffset);
    }

    public void getChars(int srcBegin, int srcEnd, @NotNull char[] dst, int dstBegin) {
        string.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    public byte[] getBytes(@NotNull String charsetName) throws UnsupportedEncodingException {
        return string.getBytes(charsetName);
    }

    public byte[] getBytes(@NotNull Charset charset) {
        return string.getBytes(charset);
    }

    public byte[] getBytes() {
        return string.getBytes();
    }

    @Override
    public boolean equals(Object anObject) {
        return string.equals(anObject);
    }

    public boolean contentEquals(@NotNull StringBuffer sb) {
        return string.contentEquals(sb);
    }

    public boolean contentEquals(@NotNull CharSequence cs) {
        return string.contentEquals(cs);
    }

    public boolean equalsIgnoreCase(String anotherString) {
        return string.equalsIgnoreCase(anotherString);
    }

    public int compareTo(@NotNull String anotherString) {
        return string.compareTo(anotherString);
    }

    public int compareToIgnoreCase(@NotNull String str) {
        return string.compareToIgnoreCase(str);
    }

    public boolean regionMatches(int toffset, @NotNull String other, int ooffset, int len) {
        return string.regionMatches(toffset, other, ooffset, len);
    }

    public boolean regionMatches(boolean ignoreCase, int toffset, @NotNull String other, int ooffset, int len) {
        return string.regionMatches(ignoreCase, toffset, other, ooffset, len);
    }

    public boolean startsWith(@NotNull String prefix, int toffset) {
        return string.startsWith(prefix, toffset);
    }

    public boolean startsWith(@NotNull String prefix) {
        return string.startsWith(prefix);
    }

    public boolean endsWith(@NotNull String suffix) {
        return string.endsWith(suffix);
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    public int indexOf(int ch) {
        return string.indexOf(ch);
    }

    public int indexOf(int ch, int fromIndex) {
        return string.indexOf(ch, fromIndex);
    }

    public int lastIndexOf(int ch) {
        return string.lastIndexOf(ch);
    }

    public int lastIndexOf(int ch, int fromIndex) {
        return string.lastIndexOf(ch, fromIndex);
    }

    public int indexOf(@NotNull String str) {
        return string.indexOf(str);
    }

    public int indexOf(@NotNull String str, int fromIndex) {
        return string.indexOf(str, fromIndex);
    }

    public int lastIndexOf(@NotNull String str) {
        return string.lastIndexOf(str);
    }

    public int lastIndexOf(@NotNull String str, int fromIndex) {
        return string.lastIndexOf(str, fromIndex);
    }

    public String substring(int beginIndex) {
        return string.substring(beginIndex);
    }

    public String substring(int beginIndex, int endIndex) {
        return string.substring(beginIndex, endIndex);
    }

    @NotNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return string.subSequence(start, end);
    }

    public String concat(@NotNull String str) {
        return string.concat(str);
    }

    public String replace(char oldChar, char newChar) {
        return string.replace(oldChar, newChar);
    }

    public boolean matches(@NotNull String regex) {
        return string.matches(regex);
    }

    public boolean contains(@NotNull CharSequence s) {
        return string.contains(s);
    }

    public String replaceFirst(@NotNull String regex, @NotNull String replacement) {
        return string.replaceFirst(regex, replacement);
    }

    public String replaceAll(@NotNull String regex, @NotNull String replacement) {
        return string.replaceAll(regex, replacement);
    }

    public String replace(@NotNull CharSequence target, @NotNull CharSequence replacement) {
        return string.replace(target, replacement);
    }

    public String[] split(@NotNull String regex, int limit) {
        return string.split(regex, limit);
    }

    public String[] split(@NotNull String regex) {
        return string.split(regex);
    }

    public static String join(CharSequence delimiter, CharSequence... elements) {
        return String.join(delimiter, elements);
    }

    public static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements) {
        return String.join(delimiter, elements);
    }

    public String toLowerCase(@NotNull Locale locale) {
        return string.toLowerCase(locale);
    }

    public String toLowerCase() {
        return string.toLowerCase();
    }

    public String toUpperCase(@NotNull Locale locale) {
        return string.toUpperCase(locale);
    }

    public String toUpperCase() {
        return string.toUpperCase();
    }

    public String trim() {
        return string.trim();
    }

    public String strip() {
        return string.strip();
    }

    public String stripLeading() {
        return string.stripLeading();
    }

    public String stripTrailing() {
        return string.stripTrailing();
    }

    public boolean isBlank() {
        return string.isBlank();
    }

    public Stream<String> lines() {
        return string.lines();
    }

    @Override
    public @NotNull String toString() {
        return joinToString();
    }

    @NotNull
    @Override
    public IntStream codePoints() {
        return string.codePoints();
    }

    public char[] toCharArray() {
        return string.toCharArray();
    }

    public static String format(@NotNull String format, Object... args) {
        return String.format(format, args);
    }

    public static String format(Locale l, @NotNull String format, Object... args) {
        return String.format(l, format, args);
    }

    public static String valueOf(Object obj) {
        return String.valueOf(obj);
    }

    public static String valueOf(@NotNull char[] data) {
        return String.valueOf(data);
    }

    public static String valueOf(@NotNull char[] data, int offset, int count) {
        return String.valueOf(data, offset, count);
    }

    public static String copyValueOf(@NotNull char[] data, int offset, int count) {
        return String.copyValueOf(data, offset, count);
    }

    public static String copyValueOf(@NotNull char[] data) {
        return String.copyValueOf(data);
    }

    public static String valueOf(boolean b) {
        return String.valueOf(b);
    }

    public static String valueOf(char c) {
        return String.valueOf(c);
    }

    public static String valueOf(int i) {
        return String.valueOf(i);
    }

    public static String valueOf(long l) {
        return String.valueOf(l);
    }

    public static String valueOf(float f) {
        return String.valueOf(f);
    }

    public static String valueOf(double d) {
        return String.valueOf(d);
    }

    public String intern() {
        return string.intern();
    }

    public String repeat(int count) {
        return string.repeat(count);
    }

    public static int compare(CharSequence cs1, CharSequence cs2) {
        return CharSequence.compare(cs1, cs2);
    }
}
