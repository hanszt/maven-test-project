package hzt;

import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

public class SpecializedStreams {

    IntSummaryStatistics intsSummaryStatistics(int... ints) {
        return IntStream.of(ints).summaryStatistics();
    }

    DoubleSummaryStatistics doublesSummaryStatistics(double... doubles) {
        return DoubleStream.of(doubles).summaryStatistics();
    }

}
