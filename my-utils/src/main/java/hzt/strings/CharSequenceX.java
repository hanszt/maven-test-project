package hzt.strings;

import hzt.collections.IterableX;
import hzt.stream.function.PredicateX;
import org.jetbrains.annotations.NotNull;

public interface CharSequenceX extends CharSequence, IterableX<Character> {

    Iterable<Character> iterable();

    static CharSequenceX of(CharSequence s) {
        return StringX.of(s);
    }

    static CharSequenceX of(IterableX<Character> s) {
        return new CharSequenceX() {
            @Override
            public Iterable<Character> iterable() {
                return s;
            }

            @Override
            public int length() {
                return s.count(PredicateX.noFilter());
            }

            @Override
            public char charAt(int index) {
                throw new UnsupportedOperationException();
            }

            @NotNull
            @Override
            public CharSequence subSequence(int start, int end) {
                throw new UnsupportedOperationException();
            }
        };
    }
}
