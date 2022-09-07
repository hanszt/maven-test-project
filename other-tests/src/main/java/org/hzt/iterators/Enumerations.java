package org.hzt.iterators;

import org.jetbrains.annotations.NotNull;

import java.util.Enumeration;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public final class Enumerations {

    private Enumerations() {
    }

    @NotNull
    public static <T> Enumeration<T> sizedEnumeration(final int size, IntFunction<T> intFunction) {
        return new Enumeration<>() {
            private int counter = 0;

            @Override
            public boolean hasMoreElements() {
                return counter < size;
            }

            @Override
            public T nextElement() {
                return intFunction.apply(counter++);
            }
        };
    }

    @NotNull
    public static <T> Enumeration<T> generatorEnumeration(T seed, Predicate<T> predicate, UnaryOperator<T> nextFunction) {
        return new Enumeration<>() {

            private T next = seed;

            @Override
            public boolean hasMoreElements() {
                return predicate.test(next);
            }

            @Override
            public T nextElement() {
                T nextElement = this.next;
                this.next = nextFunction.apply(this.next);
                return nextElement;
            }
        };
    }
}
