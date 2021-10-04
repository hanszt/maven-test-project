package hzt.stream.collectors;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MyCollectors {

    private static final Set<Collector.Characteristics> CH_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

    private static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

    private MyCollectors() {
    }

    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> toUnmodifiableMap() {
        return Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> toMap() {
        return Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <T> Collector<T, ?, List<T>> filteringToList(Predicate<? super T> predicate) {
        return Collectors.filtering(predicate, Collectors.toUnmodifiableList());
    }

    public static <T, R> Collector<T, ?, List<R>> mappingToList(Function<? super T, ? extends R> mapper) {
        return Collectors.mapping(mapper, Collectors.toUnmodifiableList());
    }

    public static <T, R> Collector<T, ?, List<R>> flatMappingToList(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return Collectors.flatMapping(mapper, Collectors.toUnmodifiableList());
    }

    public static <T> Collector<T, ?, Set<T>> filteringToSet(Predicate<? super T> predicate) {
        return Collectors.filtering(predicate, Collectors.toUnmodifiableSet());
    }

    public static <T, R> Collector<T, ?, Set<R>> mappingToSet(Function<? super T, ? extends R> mapper) {
        return Collectors.mapping(mapper, Collectors.toUnmodifiableSet());
    }

    public static <T, R> Collector<T, ?, Set<R>> flatMappingToSet(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return Collectors.flatMapping(mapper, Collectors.toUnmodifiableSet());
    }

    public static <T, R1, R2> Collector<T, ?, Map.Entry<R1, R2>> teeingToEntry(
            Collector<? super T, ?, R1> downstream1,
            Collector<? super T, ?, R2> downstream2) {
        return Collectors.teeing(downstream1, downstream2, AbstractMap.SimpleEntry::new);
    }

    /**
     * Returns a {@code Collector} that is a composite of three downstream collectors.
     * Every element passed to the resulting collector is processed by both downstream
     * collectors, then their results are merged using the specified merge function
     * into the final result.
     *
     * <p>The resulting collector functions do the following:
     *
     * <ul>
     * <li>supplier: creates a result container that contains result containers
     * obtained by calling each collector's supplier
     * <li>accumulator: calls each collector's accumulator with its result container
     * and the input element
     * <li>combiner: calls each collector's combiner with three result containers
     * <li>finisher: calls each collector's finisher with its result container,
     * then calls the supplied merger and returns its result.
     * </ul>
     *
     * <p>The resulting collector is {@link Collector.Characteristics#UNORDERED} if both downstream
     * collectors are unordered and {@link Collector.Characteristics#CONCURRENT} if both downstream
     * collectors are concurrent.
     *
     * @param <T>         the type of the input elements
     * @param <R1>        the result type of the first collector
     * @param <R2>        the result type of the second collector
     * @param <R3>        the result type of the third collector
     * @param <R>         the final result type
     * @param downstream1 the first downstream collector
     * @param downstream2 the second downstream collector
     * @param downstream3 the third downstream collector
     * @param merger      the function which merges three results into the single one
     * @return a {@code Collector} which aggregates the results of three supplied collectors.
     */
    public static <T, R1, R2, R3, R>
    Collector<T, ?, R> branching(Collector<? super T, ?, R1> downstream1,
                                 Collector<? super T, ?, R2> downstream2,
                                 Collector<? super T, ?, R3> downstream3,
                                 TriFunction<? super R1, ? super R2, ? super R3, R> merger) {
        return branching0(downstream1, downstream2, downstream3, merger);
    }

    public static <T, R1, R2, R3>
    Collector<T, ?, TriTuple<R1, R2, R3>> branching(Collector<? super T, ?, R1> downstream1,
                                                    Collector<? super T, ?, R2> downstream2,
                                                    Collector<? super T, ?, R3> downstream3) {
        return branching0(downstream1, downstream2, downstream3, MyCollectors::toTriTuple);
    }

    private static <T, A1, A2, A3, R1, R2, R3, R>
    Collector<T, ?, R> branching0(Collector<? super T, A1, R1> downstream1,
                                  Collector<? super T, A2, R2> downstream2,
                                  Collector<? super T, A3, R3> downstream3,
                                  TriFunction<? super R1, ? super R2, ? super R3, R> merger) {
        Objects.requireNonNull(downstream1, "downstream1");
        Objects.requireNonNull(downstream2, "downstream2");
        Objects.requireNonNull(downstream3, "downstream3");
        Objects.requireNonNull(merger, "merger");

        Supplier<A1> c1Supplier = Objects.requireNonNull(downstream1.supplier(), "downstream1 supplier");
        Supplier<A2> c2Supplier = Objects.requireNonNull(downstream2.supplier(), "downstream2 supplier");
        Supplier<A3> c3Supplier = Objects.requireNonNull(downstream3.supplier(), "downstream3 supplier");

        BiConsumer<A1, ? super T> c1Accumulator =
                Objects.requireNonNull(downstream1.accumulator(), "downstream1 accumulator");
        BiConsumer<A2, ? super T> c2Accumulator =
                Objects.requireNonNull(downstream2.accumulator(), "downstream2 accumulator");
        BiConsumer<A3, ? super T> c3Accumulator =
                Objects.requireNonNull(downstream3.accumulator(), "downstream3 accumulator");

        BinaryOperator<A1> c1Combiner = Objects.requireNonNull(downstream1.combiner(), "downstream1 combiner");
        BinaryOperator<A2> c2Combiner = Objects.requireNonNull(downstream2.combiner(), "downstream2 combiner");
        BinaryOperator<A3> c3Combiner = Objects.requireNonNull(downstream3.combiner(), "downstream3 combiner");

        Function<A1, R1> c1Finisher = Objects.requireNonNull(downstream1.finisher(), "downstream1 finisher");
        Function<A2, R2> c2Finisher = Objects.requireNonNull(downstream2.finisher(), "downstream2 finisher");
        Function<A3, R3> c3Finisher = Objects.requireNonNull(downstream3.finisher(), "downstream3 finisher");

        Set<Collector.Characteristics> characteristics;
        Set<Collector.Characteristics> c1Characteristics = downstream1.characteristics();
        Set<Collector.Characteristics> c2Characteristics = downstream2.characteristics();
        Set<Collector.Characteristics> c3Characteristics = downstream3.characteristics();

        if (CH_ID.containsAll(c1Characteristics) || CH_ID.containsAll(c2Characteristics) || CH_ID.containsAll(c3Characteristics)) {
            characteristics = CH_NOID;
        } else {
            EnumSet<Collector.Characteristics> c = EnumSet.noneOf(Collector.Characteristics.class);
            c.addAll(c1Characteristics);
            c.retainAll(c2Characteristics);
            c.retainAll(c3Characteristics);
            c.remove(Collector.Characteristics.IDENTITY_FINISH);
            characteristics = Collections.unmodifiableSet(c);
        }

        class TriBox {
            private A1 left = c1Supplier.get();
            private A2 middle = c2Supplier.get();
            private A3 right = c3Supplier.get();

            void add(T t) {
                c1Accumulator.accept(left, t);
                c2Accumulator.accept(middle, t);
                c3Accumulator.accept(right, t);
            }

            TriBox combine(TriBox other) {
                left = c1Combiner.apply(left, other.left);
                middle = c2Combiner.apply(middle, other.middle);
                right = c3Combiner.apply(right, other.right);
                return this;
            }

            R get() {
                R1 r1 = c1Finisher.apply(left);
                R2 r2 = c2Finisher.apply(middle);
                R3 r3 = c3Finisher.apply(right);
                return merger.apply(r1, r2, r3);
            }
        }
        return new CollectorImpl<>(TriBox::new, TriBox::add, TriBox::combine, TriBox::get, characteristics);
    }

    @FunctionalInterface
    public interface TriFunction<A1, A2, A3, R> {

        R apply(A1 a1, A2 a2, A3 a3);
    }

    /**
     * Returns a {@code Collector} that is a composite of four downstream collectors.
     * Every element passed to the resulting collector is processed by both downstream
     * collectors, then their results are merged using the specified merge function
     * into the final result.
     *
     * <p>The resulting collector functions do the following:
     *
     * <ul>
     * <li>supplier: creates a result container that contains result containers
     * obtained by calling each collector's supplier
     * <li>accumulator: calls each collector's accumulator with its result container
     * and the input element
     * <li>combiner: calls each collector's combiner with four result containers
     * <li>finisher: calls each collector's finisher with its result container,
     * then calls the supplied merger and returns its result.
     * </ul>
     *
     * <p>The resulting collector is {@link Collector.Characteristics#UNORDERED} if both downstream
     * collectors are unordered and {@link Collector.Characteristics#CONCURRENT} if both downstream
     * collectors are concurrent.
     *
     * @param <T>         the type of the input elements
     * @param <R1>        the result type of the first collector
     * @param <R2>        the result type of the second collector
     * @param <R3>        the result type of the third collector
     * @param <R4>        the result type of the fourth collector
     * @param <R>         the final result type
     * @param downstream1 the first downstream collector
     * @param downstream2 the second downstream collector
     * @param downstream3 the third downstream collector
     * @param downstream4 the fourth downstream collector
     * @param merger      the function which merges four results into the single one
     * @return a {@code Collector} which aggregates the results of four supplied collectors.
     */
    public static <T, R1, R2, R3, R4, R>
    Collector<T, ?, R> branching(Collector<? super T, ?, R1> downstream1,
                                 Collector<? super T, ?, R2> downstream2,
                                 Collector<? super T, ?, R3> downstream3,
                                 Collector<? super T, ?, R4> downstream4,
                                 QuadFunction<? super R1, ? super R2, ? super R3, ? super R4, R> merger) {
        return branching0(downstream1, downstream2, downstream3, downstream4, merger);
    }

    public static <T, R1, R2, R3, R4>
    Collector<T, ?, QuadTuple<R1, R2, R3, R4>> branching(Collector<? super T, ?, R1> downstream1,
                                                         Collector<? super T, ?, R2> downstream2,
                                                         Collector<? super T, ?, R3> downstream3,
                                                         Collector<? super T, ?, R4> downstream4) {
        return branching0(downstream1, downstream2, downstream3, downstream4, MyCollectors::toQuadTuple);
    }

    private static <T, A1, A2, A3, A4, R1, R2, R3, R4, R>
    Collector<T, ?, R> branching0(Collector<? super T, A1, R1> downstream1,
                                  Collector<? super T, A2, R2> downstream2,
                                  Collector<? super T, A3, R3> downstream3,
                                  Collector<? super T, A4, R4> downstream4,
                                  QuadFunction<? super R1, ? super R2, ? super R3, ? super R4, R> merger) {
        Objects.requireNonNull(downstream1, "downstream1");
        Objects.requireNonNull(downstream2, "downstream2");
        Objects.requireNonNull(downstream3, "downstream3");
        Objects.requireNonNull(downstream4, "downstream4");
        Objects.requireNonNull(merger, "merger");

        Supplier<A1> c1Supplier = Objects.requireNonNull(downstream1.supplier(), "downstream1 supplier");
        Supplier<A2> c2Supplier = Objects.requireNonNull(downstream2.supplier(), "downstream2 supplier");
        Supplier<A3> c3Supplier = Objects.requireNonNull(downstream3.supplier(), "downstream3 supplier");
        Supplier<A4> c4Supplier = Objects.requireNonNull(downstream4.supplier(), "downstream4 supplier");

        BiConsumer<A1, ? super T> c1Accumulator =
                Objects.requireNonNull(downstream1.accumulator(), "downstream1 accumulator");
        BiConsumer<A2, ? super T> c2Accumulator =
                Objects.requireNonNull(downstream2.accumulator(), "downstream2 accumulator");
        BiConsumer<A3, ? super T> c3Accumulator =
                Objects.requireNonNull(downstream3.accumulator(), "downstream3 accumulator");
        BiConsumer<A4, ? super T> c4Accumulator =
                Objects.requireNonNull(downstream4.accumulator(), "downstream4 accumulator");

        BinaryOperator<A1> c1Combiner = Objects.requireNonNull(downstream1.combiner(), "downstream1 combiner");
        BinaryOperator<A2> c2Combiner = Objects.requireNonNull(downstream2.combiner(), "downstream2 combiner");
        BinaryOperator<A3> c3Combiner = Objects.requireNonNull(downstream3.combiner(), "downstream3 combiner");
        BinaryOperator<A4> c4Combiner = Objects.requireNonNull(downstream4.combiner(), "downstream4 combiner");

        Function<A1, R1> c1Finisher = Objects.requireNonNull(downstream1.finisher(), "downstream1 finisher");
        Function<A2, R2> c2Finisher = Objects.requireNonNull(downstream2.finisher(), "downstream2 finisher");
        Function<A3, R3> c3Finisher = Objects.requireNonNull(downstream3.finisher(), "downstream3 finisher");
        Function<A4, R4> c4Finisher = Objects.requireNonNull(downstream4.finisher(), "downstream4 finisher");

        Set<Collector.Characteristics> characteristics;
        Set<Collector.Characteristics> c1Characteristics = downstream1.characteristics();
        Set<Collector.Characteristics> c2Characteristics = downstream2.characteristics();
        Set<Collector.Characteristics> c3Characteristics = downstream3.characteristics();
        Set<Collector.Characteristics> c4Characteristics = downstream4.characteristics();

        if (CH_ID.containsAll(c1Characteristics) || CH_ID.containsAll(c2Characteristics) ||
                CH_ID.containsAll(c3Characteristics) || CH_ID.containsAll(c4Characteristics)) {
            characteristics = CH_NOID;
        } else {
            EnumSet<Collector.Characteristics> c = EnumSet.noneOf(Collector.Characteristics.class);
            c.addAll(c1Characteristics);
            c.retainAll(c2Characteristics);
            c.retainAll(c3Characteristics);
            c.retainAll(c4Characteristics);
            c.remove(Collector.Characteristics.IDENTITY_FINISH);
            characteristics = Collections.unmodifiableSet(c);
        }

        class QuadBox {
            private A1 left = c1Supplier.get();
            private A2 middleLeft = c2Supplier.get();
            private A3 middleRight = c3Supplier.get();
            private A4 right = c4Supplier.get();

            void add(T t) {
                c1Accumulator.accept(left, t);
                c2Accumulator.accept(middleLeft, t);
                c3Accumulator.accept(middleRight, t);
                c4Accumulator.accept(right, t);
            }

            QuadBox combine(QuadBox other) {
                left = c1Combiner.apply(left, other.left);
                middleLeft = c2Combiner.apply(middleLeft, other.middleLeft);
                middleRight = c3Combiner.apply(middleRight, other.middleRight);
                right = c4Combiner.apply(right, other.right);
                return this;
            }

            R get() {
                R1 r1 = c1Finisher.apply(left);
                R2 r2 = c2Finisher.apply(middleLeft);
                R3 r3 = c3Finisher.apply(middleRight);
                R4 r4 = c4Finisher.apply(right);
                return merger.apply(r1, r2, r3, r4);
            }
        }
        return new CollectorImpl<>(QuadBox::new, QuadBox::add, QuadBox::combine, QuadBox::get, characteristics);
    }

    @FunctionalInterface
    public interface QuadFunction<A1, A2, A3, A4, R> {

        R apply(A1 a1, A2 a2, A3 a3, A4 a4);
    }

    public static <R1, R2, R3> TriTuple<R1, R2, R3> toTriTuple(R1 r1, R2 r2, R3 r3) {
        return new TriTuple<>(r1, r2, r3);
    }

    public record TriTuple<R1, R2, R3>(R1 first, R2 second, R3 third) {
    }

    public static <R1, R2, R3, R4> QuadTuple<R1, R2, R3, R4> toQuadTuple(R1 r1, R2 r2, R3 r3, R4 r4) {
        return new QuadTuple<>(r1, r2, r3, r4);
    }

    public record QuadTuple<R1, R2, R3, R4>(R1 first, R2 second, R3 third, R4 fourth) {
    }
}
