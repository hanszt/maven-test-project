package hzt.strings;

import hzt.collections.IterableX;
import hzt.collections.MutableListX;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

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

    public MutableListX<Character> toMutableList() {
        return toMutableListOf(Function.identity());
    }

    @Override
    public int length() {
        return string.length();
    }

    @Override
    public char charAt(int index) {
        return string.charAt(index);
    }

    @NotNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return string.subSequence(start, end);
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
    public @NotNull String toString() {
        return joinToString();
    }
}
