package com.dnb.custom_collectors;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public final class MyCollectors {

    private static final Set<Collector.Characteristics> CH_ID
            = Collections.unmodifiableSet(EnumSet.of(Collector.Characteristics.IDENTITY_FINISH));

    private static final Set<Collector.Characteristics> CH_NOID = Collections.emptySet();

    private MyCollectors() {
    }

    public static Collector<BigDecimal, BigDecimalAccumulator, BigDecimal> toBigDecimalAverage() {
        return new CollectorImpl<>(BigDecimalAccumulator::new,
                BigDecimalAccumulator::accumulate,
                BigDecimalAccumulator::combine,
                BigDecimalAccumulator::getAverage,
                CH_NOID);
    }

    public static Collector<BigDecimal, BigDecimalAccumulator, BigDecimalSummaryStatistics> toBigDecimalSummaryStatistics() {
        return new CollectorImpl<>(BigDecimalAccumulator::new,
                BigDecimalAccumulator::accumulate,
                BigDecimalAccumulator::combine,
                BigDecimalAccumulator::getSummaryStatistics,
                CH_NOID);
    }

    /**
     * Returns a {@code Collector} that is a composite of two downstream collectors.
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
     * <li>combiner: calls each collector's combiner with two result containers
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
     * @param <R>         the final result type
     * @param downstream1 the first downstream collector
     * @param downstream2 the second downstream collector
     * @param merger      the function which merges two results into the single one
     * @return a {@code Collector} which aggregates the results of two supplied collectors.
     */
    public static <T, R1, R2, R>
    Collector<T, ?, R> teeing(Collector<? super T, ?, R1> downstream1,
                              Collector<? super T, ?, R2> downstream2,
                              BiFunction<? super R1, ? super R2, R> merger) {
        return teeing0(downstream1, downstream2, merger);
    }

    private static <T, A1, A2, R1, R2, R>
    Collector<T, ?, R> teeing0(Collector<? super T, A1, R1> downstream1,
                               Collector<? super T, A2, R2> downstream2,
                               BiFunction<? super R1, ? super R2, R> merger) {
        Objects.requireNonNull(downstream1, "downstream1");
        Objects.requireNonNull(downstream2, "downstream2");
        Objects.requireNonNull(merger, "merger");

        Supplier<A1> c1Supplier = Objects.requireNonNull(downstream1.supplier(), "downstream1 supplier");
        Supplier<A2> c2Supplier = Objects.requireNonNull(downstream2.supplier(), "downstream2 supplier");
        BiConsumer<A1, ? super T> c1Accumulator =
                Objects.requireNonNull(downstream1.accumulator(), "downstream1 accumulator");
        BiConsumer<A2, ? super T> c2Accumulator =
                Objects.requireNonNull(downstream2.accumulator(), "downstream2 accumulator");
        BinaryOperator<A1> c1Combiner = Objects.requireNonNull(downstream1.combiner(), "downstream1 combiner");
        BinaryOperator<A2> c2Combiner = Objects.requireNonNull(downstream2.combiner(), "downstream2 combiner");
        Function<A1, R1> c1Finisher = Objects.requireNonNull(downstream1.finisher(), "downstream1 finisher");
        Function<A2, R2> c2Finisher = Objects.requireNonNull(downstream2.finisher(), "downstream2 finisher");

        Set<Collector.Characteristics> characteristics;
        Set<Collector.Characteristics> c1Characteristics = downstream1.characteristics();
        Set<Collector.Characteristics> c2Characteristics = downstream2.characteristics();
        if (CH_ID.containsAll(c1Characteristics) || CH_ID.containsAll(c2Characteristics)) {
            characteristics = CH_NOID;
        } else {
            EnumSet<Collector.Characteristics> c = EnumSet.noneOf(Collector.Characteristics.class);
            c.addAll(c1Characteristics);
            c.retainAll(c2Characteristics);
            c.remove(Collector.Characteristics.IDENTITY_FINISH);
            characteristics = Collections.unmodifiableSet(c);
        }

        class PairBox {
            private A1 left = c1Supplier.get();
            private A2 right = c2Supplier.get();

            void add(T t) {
                c1Accumulator.accept(left, t);
                c2Accumulator.accept(right, t);
            }

            PairBox combine(PairBox other) {
                left = c1Combiner.apply(left, other.left);
                right = c2Combiner.apply(right, other.right);
                return this;
            }

            R get() {
                R1 r1 = c1Finisher.apply(left);
                R2 r2 = c2Finisher.apply(right);
                return merger.apply(r1, r2);
            }
        }

        return new CollectorImpl<>(PairBox::new, PairBox::add, PairBox::combine, PairBox::get, characteristics);
    }
}
