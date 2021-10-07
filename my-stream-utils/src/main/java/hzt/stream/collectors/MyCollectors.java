package hzt.stream.collectors;

import hzt.stream.StreamUtils;
import hzt.stream.function.QuadFunction;
import hzt.stream.function.QuintFunction;
import hzt.stream.function.TriFunction;
import hzt.stream.utils.MyCollections;
import hzt.stream.utils.MyObjects;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MyCollectors {

    public static final String MERGER = "merger";

    public static final String DOWNSTREAM_1_SUPPLIER = "downstream1 supplier";
    public static final String DOWNSTREAM_2_SUPPLIER = "downstream2 supplier";
    public static final String DOWNSTREAM_3_SUPPLIER = "downstream3 supplier";
    public static final String DOWNSTREAM_4_SUPPLIER = "downstream4 supplier";
    public static final String DOWNSTREAM_5_SUPPLIER = "downstream5 supplier";

    public static final String DOWNSTREAM_1_ACCUMULATOR = "downstream1 accumulator";
    public static final String DOWNSTREAM_2_ACCUMULATOR = "downstream2 accumulator";
    public static final String DOWNSTREAM_3_ACCUMULATOR = "downstream3 accumulator";
    public static final String DOWNSTREAM_4_ACCUMULATOR = "downstream4 accumulator";
    public static final String DOWNSTREAM_5_ACCUMULATOR = "downstream5 accumulator";

    public static final String DOWNSTREAM_1_COMBINER = "downstream1 combiner";
    public static final String DOWNSTREAM_2_COMBINER = "downstream2 combiner";
    public static final String DOWNSTREAM_3_COMBINER = "downstream3 combiner";
    public static final String DOWNSTREAM_4_COMBINER = "downstream4 combiner";
    public static final String DOWNSTREAM_5_COMBINER = "downstream5 combiner";

    public static final String DOWNSTREAM_1_FINISHER = "downstream1 finisher";
    public static final String DOWNSTREAM_2_FINISHER = "downstream2 finisher";
    public static final String DOWNSTREAM_3_FINISHER = "downstream3 finisher";
    public static final String DOWNSTREAM_4_FINISHER = "downstream4 finisher";
    public static final String DOWNSTREAM_5_FINISHER = "downstream5 finisher";

    private MyCollectors() {
    }

    public static <T, U, A, R> Collector<T, ?, R> multiMapping(BiConsumer<? super T, ? super Consumer<U>> mapper,
                                                               Collector<? super U, A, R> downstream) {
        final BiConsumer<A, T> accumulator = (A a, T t) -> {
            final Consumer<U> uConsumer = (U u) -> downstream.accumulator().accept(a, u);
            mapper.accept(t, uConsumer);
        };
        return new CollectorImpl<>(downstream.supplier(),
                accumulator,
                downstream.combiner(),
                downstream.finisher(),
                downstream.characteristics());
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

    public static <T, R> Collector<T, ?, List<R>> multiMappingToList(BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return multiMapping(mapper, Collectors.toUnmodifiableList());
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

    public static <T, R> Collector<T, ?, Set<R>> multiMappingToSet(BiConsumer<? super T, ? super Consumer<R>> mapper) {
        return multiMapping(mapper, Collectors.toUnmodifiableSet());
    }

    public static <T, R> Collector<T, ?, Set<R>> flatMappingToSet(Function<? super T, ? extends Stream<? extends R>> mapper) {
        return Collectors.flatMapping(mapper, Collectors.toUnmodifiableSet());
    }

    public static <T, A, K> Collector<T, ?, Map<K, List<T>>> groupingBy(Function<? super T, ? extends A> classifierPart1,
                                                                        Function<? super A, ? extends K> classifierPart2) {
        return Collectors.groupingBy(StreamUtils.function(classifierPart1).andThen(classifierPart2));
    }

    public static <T, R1, R2> Collector<T, ?, Map.Entry<R1, R2>> teeingToEntry(
            Collector<? super T, ?, R1> downstream1,
            Collector<? super T, ?, R2> downstream2) {
        return Collectors.teeing(downstream1, downstream2, AbstractMap.SimpleEntry::new);
    }

    public static <T> Collector<T, ?, DoubleStatistics> toDoubleStatisticsBy(ToDoubleFunction<? super T> toDoubleFunction) {
        Objects.requireNonNull(toDoubleFunction);
        return Collector.of(
                DoubleStatistics::new,
                (doubleStatistics, value) -> doubleStatistics.accept(toDoubleFunction.applyAsDouble(value)),
                DoubleStatistics::combine,
                DoubleStatistics::getDoubleStatistics
        );
    }

    public static <T> Collector<T, ?, Double> standardDeviatingDouble(ToDoubleFunction<? super T> toDoubleFunction) {
        Objects.requireNonNull(toDoubleFunction);
        return Collector.of(
                DoubleStatistics::new,
                (doubleStatistics, value) -> doubleStatistics.accept(toDoubleFunction.applyAsDouble(value)),
                DoubleStatistics::combine,
                DoubleStatistics::getStandardDeviation
        );
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
     * <p>The resulting collector is {@link Collector.Characteristics#UNORDERED} if all downstream
     * collectors are unordered and {@link Collector.Characteristics#CONCURRENT} if all downstream
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
        return branching0(downstream1, downstream2, downstream3, TriTuple::new);
    }

    private static <T, A1, A2, A3, R1, R2, R3, R>
    Collector<T, ?, R> branching0(Collector<? super T, A1, R1> downstream1,
                                  Collector<? super T, A2, R2> downstream2,
                                  Collector<? super T, A3, R3> downstream3,
                                  TriFunction<? super R1, ? super R2, ? super R3, R> merger) {
        MyObjects.requireAllNonNull(Collector.class, downstream1, downstream2, downstream3);
        Objects.requireNonNull(merger, MERGER);

        Supplier<A1> c1Supplier = Objects.requireNonNull(downstream1.supplier(), DOWNSTREAM_1_SUPPLIER);
        Supplier<A2> c2Supplier = Objects.requireNonNull(downstream2.supplier(), DOWNSTREAM_2_SUPPLIER);
        Supplier<A3> c3Supplier = Objects.requireNonNull(downstream3.supplier(), DOWNSTREAM_3_SUPPLIER);

        BiConsumer<A1, ? super T> c1Accumulator =
                Objects.requireNonNull(downstream1.accumulator(), DOWNSTREAM_1_ACCUMULATOR);
        BiConsumer<A2, ? super T> c2Accumulator =
                Objects.requireNonNull(downstream2.accumulator(), DOWNSTREAM_2_ACCUMULATOR);
        BiConsumer<A3, ? super T> c3Accumulator =
                Objects.requireNonNull(downstream3.accumulator(), DOWNSTREAM_3_ACCUMULATOR);

        BinaryOperator<A1> c1Combiner = Objects.requireNonNull(downstream1.combiner(), DOWNSTREAM_1_COMBINER);
        BinaryOperator<A2> c2Combiner = Objects.requireNonNull(downstream2.combiner(), DOWNSTREAM_2_COMBINER);
        BinaryOperator<A3> c3Combiner = Objects.requireNonNull(downstream3.combiner(), DOWNSTREAM_3_COMBINER);

        Function<A1, R1> c1Finisher = Objects.requireNonNull(downstream1.finisher(), DOWNSTREAM_1_FINISHER);
        Function<A2, R2> c2Finisher = Objects.requireNonNull(downstream2.finisher(), DOWNSTREAM_2_FINISHER);
        Function<A3, R3> c3Finisher = Objects.requireNonNull(downstream3.finisher(), DOWNSTREAM_3_FINISHER);

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
        Set<Collector.Characteristics> characteristics = evaluateCharacteristics(downstream1, downstream2, downstream3);
        return new CollectorImpl<>(TriBox::new, TriBox::add, TriBox::combine, TriBox::get, characteristics);
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
     * <p>The resulting collector is {@link Collector.Characteristics#UNORDERED} if all downstream
     * collectors are unordered and {@link Collector.Characteristics#CONCURRENT} if all downstream
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
        return branching0(downstream1, downstream2, downstream3, downstream4, QuadTuple::new);
    }

    private static <T, A1, A2, A3, A4, R1, R2, R3, R4, R>
    Collector<T, ?, R> branching0(Collector<? super T, A1, R1> downstream1,
                                  Collector<? super T, A2, R2> downstream2,
                                  Collector<? super T, A3, R3> downstream3,
                                  Collector<? super T, A4, R4> downstream4,
                                  QuadFunction<? super R1, ? super R2, ? super R3, ? super R4, R> merger) {
        MyObjects.requireAllNonNull(Collector.class, downstream1, downstream2, downstream3, downstream4);
        Objects.requireNonNull(merger, MERGER);

        Supplier<A1> c1Supplier = Objects.requireNonNull(downstream1.supplier(), DOWNSTREAM_1_SUPPLIER);
        Supplier<A2> c2Supplier = Objects.requireNonNull(downstream2.supplier(), DOWNSTREAM_2_SUPPLIER);
        Supplier<A3> c3Supplier = Objects.requireNonNull(downstream3.supplier(), DOWNSTREAM_3_SUPPLIER);
        Supplier<A4> c4Supplier = Objects.requireNonNull(downstream4.supplier(), DOWNSTREAM_4_SUPPLIER);

        BiConsumer<A1, ? super T> c1Accumulator =
                Objects.requireNonNull(downstream1.accumulator(), DOWNSTREAM_1_ACCUMULATOR);
        BiConsumer<A2, ? super T> c2Accumulator =
                Objects.requireNonNull(downstream2.accumulator(), DOWNSTREAM_2_ACCUMULATOR);
        BiConsumer<A3, ? super T> c3Accumulator =
                Objects.requireNonNull(downstream3.accumulator(), DOWNSTREAM_3_ACCUMULATOR);
        BiConsumer<A4, ? super T> c4Accumulator =
                Objects.requireNonNull(downstream4.accumulator(), DOWNSTREAM_4_ACCUMULATOR);

        BinaryOperator<A1> c1Combiner = Objects.requireNonNull(downstream1.combiner(), DOWNSTREAM_1_COMBINER);
        BinaryOperator<A2> c2Combiner = Objects.requireNonNull(downstream2.combiner(), DOWNSTREAM_2_COMBINER);
        BinaryOperator<A3> c3Combiner = Objects.requireNonNull(downstream3.combiner(), DOWNSTREAM_3_COMBINER);
        BinaryOperator<A4> c4Combiner = Objects.requireNonNull(downstream4.combiner(), DOWNSTREAM_4_COMBINER);

        Function<A1, R1> c1Finisher = Objects.requireNonNull(downstream1.finisher(), DOWNSTREAM_1_FINISHER);
        Function<A2, R2> c2Finisher = Objects.requireNonNull(downstream2.finisher(), DOWNSTREAM_2_FINISHER);
        Function<A3, R3> c3Finisher = Objects.requireNonNull(downstream3.finisher(), DOWNSTREAM_3_FINISHER);
        Function<A4, R4> c4Finisher = Objects.requireNonNull(downstream4.finisher(), DOWNSTREAM_4_FINISHER);

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
        Set<Collector.Characteristics> characteristics = evaluateCharacteristics(downstream1, downstream2, downstream3, downstream4);
        return new CollectorImpl<>(QuadBox::new, QuadBox::add, QuadBox::combine, QuadBox::get, characteristics);
    }

    /**
     * Returns a {@code Collector} that is a composite of five downstream collectors.
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
     * <li>combiner: calls each collector's combiner with five result containers
     * <li>finisher: calls each collector's finisher with its result container,
     * then calls the supplied merger and returns its result.
     * </ul>
     *
     * <p>The resulting collector is {@link Collector.Characteristics#UNORDERED} if all downstream
     * collectors are unordered and {@link Collector.Characteristics#CONCURRENT} if all downstream
     * collectors are concurrent.
     *
     * @param <T>         the type of the input elements
     * @param <R1>        the result type of the first collector
     * @param <R2>        the result type of the second collector
     * @param <R3>        the result type of the third collector
     * @param <R4>        the result type of the fourth collector
     * @param <R5>        the result type of the fifth collector
     * @param <R>         the final result type
     * @param downstream1 the first downstream collector
     * @param downstream2 the second downstream collector
     * @param downstream3 the third downstream collector
     * @param downstream4 the fourth downstream collector
     * @param downstream5 the fifth downstream collector
     * @param merger      the function which merges five results into the single one
     * @return a {@code Collector} which aggregates the results of five supplied collectors.
     */
    public static <T, R1, R2, R3, R4, R5, R>
    Collector<T, ?, R> branching(Collector<? super T, ?, R1> downstream1,
                                 Collector<? super T, ?, R2> downstream2,
                                 Collector<? super T, ?, R3> downstream3,
                                 Collector<? super T, ?, R4> downstream4,
                                 Collector<? super T, ?, R5> downstream5,
                                 QuintFunction<? super R1, ? super R2, ? super R3, ? super R4, ? super R5, R> merger) {
        return branching0(downstream1, downstream2, downstream3, downstream4, downstream5, merger);
    }

    public static <T, R1, R2, R3, R4, R5>
    Collector<T, ?, QuintTuple<R1, R2, R3, R4, R5>> branching(Collector<? super T, ?, R1> downstream1,
                                                              Collector<? super T, ?, R2> downstream2,
                                                              Collector<? super T, ?, R3> downstream3,
                                                              Collector<? super T, ?, R4> downstream4,
                                                              Collector<? super T, ?, R5> downstream5) {
        return branching0(downstream1, downstream2, downstream3, downstream4, downstream5, QuintTuple::new);
    }

    private static <T, A1, A2, A3, A4, A5, R1, R2, R3, R4, R5, R>
    Collector<T, ?, R> branching0(Collector<? super T, A1, R1> downstream1,
                                  Collector<? super T, A2, R2> downstream2,
                                  Collector<? super T, A3, R3> downstream3,
                                  Collector<? super T, A4, R4> downstream4,
                                  Collector<? super T, A5, R5> downstream5,
                                  QuintFunction<? super R1, ? super R2, ? super R3, ? super R4, ? super R5, R> merger) {
        MyObjects.requireAllNonNull(Collector.class, downstream1, downstream2, downstream3, downstream4, downstream5);
        Objects.requireNonNull(merger, MERGER);

        Supplier<A1> c1Supplier = Objects.requireNonNull(downstream1.supplier(), DOWNSTREAM_1_SUPPLIER);
        Supplier<A2> c2Supplier = Objects.requireNonNull(downstream2.supplier(), DOWNSTREAM_2_SUPPLIER);
        Supplier<A3> c3Supplier = Objects.requireNonNull(downstream3.supplier(), DOWNSTREAM_3_SUPPLIER);
        Supplier<A4> c4Supplier = Objects.requireNonNull(downstream4.supplier(), DOWNSTREAM_4_SUPPLIER);
        Supplier<A5> c5Supplier = Objects.requireNonNull(downstream5.supplier(), DOWNSTREAM_5_SUPPLIER);

        BiConsumer<A1, ? super T> c1Accumulator =
                Objects.requireNonNull(downstream1.accumulator(), DOWNSTREAM_1_ACCUMULATOR);
        BiConsumer<A2, ? super T> c2Accumulator =
                Objects.requireNonNull(downstream2.accumulator(), DOWNSTREAM_2_ACCUMULATOR);
        BiConsumer<A3, ? super T> c3Accumulator =
                Objects.requireNonNull(downstream3.accumulator(), DOWNSTREAM_3_ACCUMULATOR);
        BiConsumer<A4, ? super T> c4Accumulator =
                Objects.requireNonNull(downstream4.accumulator(), DOWNSTREAM_4_ACCUMULATOR);
        BiConsumer<A5, ? super T> c5Accumulator =
                Objects.requireNonNull(downstream5.accumulator(), DOWNSTREAM_5_ACCUMULATOR);

        BinaryOperator<A1> c1Combiner = Objects.requireNonNull(downstream1.combiner(), DOWNSTREAM_1_COMBINER);
        BinaryOperator<A2> c2Combiner = Objects.requireNonNull(downstream2.combiner(), DOWNSTREAM_2_COMBINER);
        BinaryOperator<A3> c3Combiner = Objects.requireNonNull(downstream3.combiner(), DOWNSTREAM_3_COMBINER);
        BinaryOperator<A4> c4Combiner = Objects.requireNonNull(downstream4.combiner(), DOWNSTREAM_4_COMBINER);
        BinaryOperator<A5> c5Combiner = Objects.requireNonNull(downstream5.combiner(), DOWNSTREAM_5_COMBINER);

        Function<A1, R1> c1Finisher = Objects.requireNonNull(downstream1.finisher(), DOWNSTREAM_1_FINISHER);
        Function<A2, R2> c2Finisher = Objects.requireNonNull(downstream2.finisher(), DOWNSTREAM_2_FINISHER);
        Function<A3, R3> c3Finisher = Objects.requireNonNull(downstream3.finisher(), DOWNSTREAM_3_FINISHER);
        Function<A4, R4> c4Finisher = Objects.requireNonNull(downstream4.finisher(), DOWNSTREAM_4_FINISHER);
        Function<A5, R5> c5Finisher = Objects.requireNonNull(downstream5.finisher(), DOWNSTREAM_5_FINISHER);

        class QuintBox {
            private A1 left = c1Supplier.get();
            private A2 middleLeft = c2Supplier.get();
            private A3 middle = c3Supplier.get();
            private A4 middleRight = c4Supplier.get();
            private A5 right = c5Supplier.get();

            void add(T t) {
                c1Accumulator.accept(left, t);
                c2Accumulator.accept(middleLeft, t);
                c3Accumulator.accept(middle, t);
                c4Accumulator.accept(middleRight, t);
                c5Accumulator.accept(right, t);
            }

            QuintBox combine(QuintBox other) {
                left = c1Combiner.apply(left, other.left);
                middleLeft = c2Combiner.apply(middleLeft, other.middleLeft);
                middle = c3Combiner.apply(middle, other.middle);
                middleRight = c4Combiner.apply(middleRight, other.middleRight);
                right = c5Combiner.apply(right, other.right);
                return this;
            }

            R get() {
                R1 r1 = c1Finisher.apply(left);
                R2 r2 = c2Finisher.apply(middleLeft);
                R3 r3 = c3Finisher.apply(middle);
                R4 r4 = c4Finisher.apply(middleRight);
                R5 r5 = c5Finisher.apply(right);
                return merger.apply(r1, r2, r3, r4, r5);
            }
        }
        Set<Collector.Characteristics> characteristics = evaluateCharacteristics(
                downstream1, downstream2, downstream3, downstream4, downstream5);
        return new CollectorImpl<>(QuintBox::new, QuintBox::add, QuintBox::combine, QuintBox::get, characteristics);
    }

    public static <S extends Collection<T>, T> Collector<S, ?, Set<T>> toIntersection() {
        class Accumulator {
            private Set<T> result = null;

            void accept(S s) {
                if (result == null) {
                    result = new HashSet<>(s);
                } else {
                    result.retainAll(s);
                }
            }

            Accumulator combine(Accumulator other) {
                if (result == null) {
                    return other;
                }
                if (other.result != null) {
                    result.retainAll(other.result);
                }
                return this;
            }

            public Set<T> getResult() {
                return result != null ? result : Collections.emptySet();
            }
        }
        return Collector.of(Accumulator::new, Accumulator::accept, Accumulator::combine, Accumulator::getResult,
                Collector.Characteristics.UNORDERED);
    }

    public static <S extends Collection<T>, T, R>
    Collector<S, ?, Set<R>> intersectingBy(Function<? super T, ? extends R> toTestValMapper) {
        class Accumulator {

            private Set<R> result = null;

            void accept(S collection) {
                final Set<? extends R> set = collection.stream()
                        .map(toTestValMapper)
                        .collect(Collectors.toUnmodifiableSet());
                if (result == null) {
                    result = new HashSet<>(set);
                } else {
                    result.retainAll(set);
                }
            }

            Accumulator combine(Accumulator other) {
                if (result == null) {
                    return other;
                }
                if (other.result != null) {
                    result.retainAll(other.result);
                }
                return this;
            }

            public Set<R> getResult() {
                return result != null ? result : Collections.emptySet();
            }
        }
        return Collector.of(Accumulator::new, Accumulator::accept, Accumulator::combine, Accumulator::getResult,
                Collector.Characteristics.UNORDERED);
    }

    private static Set<Collector.Characteristics> evaluateCharacteristics(Collector<?, ?, ?>... collectors) {
        boolean anyMatchOnChIDContainsAll = Stream.of(collectors)
                .map(Collector::characteristics)
                .anyMatch(c -> c.contains(Collector.Characteristics.IDENTITY_FINISH));

        if (anyMatchOnChIDContainsAll) {
            return Collections.emptySet();
        }
        return MyCollections.intersect(Stream.of(collectors)
                .map(Collector::characteristics)
                .toList());
    }

}
