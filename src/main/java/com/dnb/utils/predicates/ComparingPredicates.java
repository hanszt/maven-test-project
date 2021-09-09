package com.dnb.utils.predicates;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public final class ComparingPredicates {

    private ComparingPredicates() {
    }

    public static Predicate<Integer> greaterThan(final int other) {
        return value -> value > other;
    }

    public static Predicate<Integer> greaterThanOrEq(final int other) {
        return value -> value >= other;
    }

    public static Predicate<Integer> smallerThan(final int other) {
        return value -> value < other;
    }

    public static Predicate<Integer> smallerThanOrEq(final int other) {
        return value -> value <= other;
    }

    public static IntPredicate greaterThanInt(final int other) {
        return value -> value > other;
    }

    public static IntPredicate smallerThanInt(final int other) {
        return value -> value < other;
    }

    public static IntPredicate greaterThanOrEqInt(final int other) {
        return value -> value >= other;
    }

    public static IntPredicate smallerThanOrEqInt(final int other) {
        return value -> value <= other;
    }

    public static <T> Predicate<Collection<T>> collectionGreaterThan(int size) {
        return collection -> collection.size() > size;
    }

    public static <T> Predicate<Collection<T>> collectionSmallerThan(int size) {
        return collection -> collection.size() < size;
    }

    public static <T> Predicate<T[]> arrayGreaterThan(int size) {
        return array -> array.length > size;
    }

    public static <T> Predicate<T[]> arraySmallerThan(int size) {
        return array -> array.length < size;
    }

    public static Predicate<int[]> intArrayGreaterThan(int size) {
        return array -> array.length > size;
    }

    public static Predicate<int[]> intArraySmallerThan(int size) {
        return array -> array.length < size;
    }

}
