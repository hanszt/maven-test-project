package benchmark;

import org.hzt.generators.Generator;
import org.hzt.utils.sequences.Sequence;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class GeneratorBenchmark {

    private final Generator.Builder<Long> fibonacciBuilder = Generator.yieldingFrom(scope -> {
        var cur = 0L;
        var next = 1L;
        //noinspection InfiniteLoopStatement
        while (true) {
            scope.yieldNext(next); // next Fibonacci number
            final var newNext = cur + next;
            cur = next;
            next = newNext;
        }
    });

    public GeneratorBenchmark() {
        super();
    }

    @Benchmark
    public List<Long> fibonacciFromGenerator() {
        return fibonacciBuilder.useAsSequence(s -> s.take(50).toList());
    }

    @Benchmark
    public List<Long> conventionalFibonacci() {
        final var longs = new ArrayList<Long>();
        var counter = 0;
        var cur = 0L;
        var next = 1L;
        while (counter < 50) {
            longs.add(next); // next Fibonacci number
            final var newNext = cur + next;
            cur = next;
            next = newNext;
            counter++;
        }
        return longs;
    }

    @Benchmark
    public List<Long> fibonacciSequence() {
        class Pair {

            final long first;
            final long second;

            public Pair(long first, long second) {
                this.first = first;
                this.second = second;
            }

            Pair next() {
                return new Pair(second, first + second);
            }
        }
        return Sequence.iterate(new Pair(0, 1), Pair::next).map(p -> p.second).take(50).toList();
    }

    public static void main(String[] args) {
        var options = new OptionsBuilder()
                .include(GeneratorBenchmark.class.getSimpleName())
                .forks(2)
                .warmupIterations(2)
                .measurementIterations(3)
                .shouldFailOnError(true)
                .build();
        try {
            new Runner(options).run();
        } catch (RunnerException e) {
            throw new IllegalStateException(e);
        }
    }
}
