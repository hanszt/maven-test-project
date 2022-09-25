package benchmark;

import org.hzt.Streams;
import org.hzt.utils.sequences.Sequence;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class FibSequenceBenchmark {

    private static final Random RANDOM = new Random(0);
    public static final String LIST_SIZE_AS_STRING = "1000";
    private static final int FIB_NR_COUNT = Integer.parseInt(LIST_SIZE_AS_STRING);

    @Param({LIST_SIZE_AS_STRING})
    private int fibonacciNrCount;

    public FibSequenceBenchmark() {
        super();
    }

    @Benchmark
    public List<BigInteger> bigIntFibonacciStreamToList() {
        return Streams.fibonacciStream()
                .limit(fibonacciNrCount)
                .toList();
    }

    @Benchmark
    public List<BigInteger> bigIntFibonacciStreamUsingRecordToList() {
        return Streams.fibonacciStreamV2()
                .limit(fibonacciNrCount)
                .toList();
    }

    @Benchmark
    public List<BigInteger> bigIntFibonacciSequenceToList() {
        return fibonacciSSequence()
                .take(fibonacciNrCount)
                .toList();
    }

    private static Sequence<BigInteger> fibonacciSSequence() {
        final BigInteger[] seed = {ZERO, ONE};
        return Sequence.generate(seed, fibPair -> new BigInteger[]{fibPair[1], fibPair[0].add(fibPair[1])})
                .map(fibPair -> fibPair[0]);
    }


    public static void main(String[] args) {
        Options options = new OptionsBuilder()
                .include(FibSequenceBenchmark.class.getSimpleName())
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
