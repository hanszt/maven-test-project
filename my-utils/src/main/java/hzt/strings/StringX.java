package hzt.strings;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public final class StringX implements CharSequenceX {

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

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @NotNull
    @Override
    public CharSequence subSequence(int start, int end) {
        return string.subSequence(start, end);
    }

    @Override
    public Iterable<Character> iterable() {
        return stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public @NotNull String toString() {
        return "StringX{" +
                "string='" + string + '\'' +
                '}';
    }
}
