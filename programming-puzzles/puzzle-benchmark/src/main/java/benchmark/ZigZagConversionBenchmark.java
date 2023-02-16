package benchmark;

import hzt.StringPuzzlesKt;
import hzt.leetcode.HztStringFun;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@SuppressWarnings("unused")
@State(Scope.Benchmark)
public class ZigZagConversionBenchmark {

    private static final String STRING_TO_CONVERT = "This is a test to convert by zigzag conversion";
    private static final String NR_OF_COLUMNS_AS_STRING = "7";

    private static final int NR_OF_COLUMNS = Integer.parseInt(NR_OF_COLUMNS_AS_STRING);


    @Param({STRING_TO_CONVERT})
    private String stringToConvert;

    @Param({NR_OF_COLUMNS_AS_STRING})
    private int nrOfColumns;

    public ZigZagConversionBenchmark() {
        super();
    }

    @Benchmark
    public String javaZigZagConversion() {
        return HztStringFun.zigZagConversionMostVotedSolution(STRING_TO_CONVERT, NR_OF_COLUMNS);
    }

    @Benchmark
    @SuppressWarnings("squid:S2384")
    public String kotlinZigZagConversion() {
        return StringPuzzlesKt.zigZagConversion(STRING_TO_CONVERT, NR_OF_COLUMNS);
    }

    public static void main(String... args) {
        Options options = new OptionsBuilder()
                .include(ZigZagConversionBenchmark.class.getSimpleName())
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
