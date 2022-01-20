package hzt.stream.collectors;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static hzt.stream.collectors.BigDecimalCollectors.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BigDecimalCollectorsTest {

    @Test
    void testSummarizingBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        System.out.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(System.out::println);

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        System.out.println("bigDecimalSummaryStatistics = " + bigDecimalSummaryStatistics);

        assertAll(
                () -> assertEquals(BigDecimal.valueOf(46502.27), bigDecimalSummaryStatistics.getAverage()),
                () -> assertEquals(BigDecimal.valueOf(232511.34), bigDecimalSummaryStatistics.getSum()),
                () -> assertEquals(BigDecimal.valueOf(-4323), bigDecimalSummaryStatistics.getMin()),
                () -> assertEquals(BigDecimal.valueOf(234235.34), bigDecimalSummaryStatistics.getMax()),
                () -> assertEquals(5, bigDecimalSummaryStatistics.getCount())
        );
    }

    @Test
    void testAveragingBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        System.out.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(System.out::println);

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var average = sampleBankAccountList.stream()
                .collect(averagingBigDecimal(BankAccount::getBalance));

        System.out.println("average = " + average);

        final var expected = bigDecimalSummaryStatistics.getAverage();
        assertEquals(average, expected);
    }

    @Test
    void testStandardDeviatingBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        System.out.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(System.out::println);

        final var doubleStatistics = sampleBankAccountList.stream()
                .map(BankAccount::getBalance)
                .collect(CollectorsX.toDoubleStatisticsBy(BigDecimal::doubleValue));

        final var expectedStandardDeviationFromDouble = BigDecimal.valueOf(doubleStatistics.getStandardDeviation())
                .setScale(2, RoundingMode.HALF_UP);

        final var bigDecimalStatistics = sampleBankAccountList.stream()
                .collect(toBigDecimalStatisticsBy(BankAccount::getBalance));
        final var expected = bigDecimalStatistics.getStandardDeviation();

        final var standarDeviationBalances = sampleBankAccountList.stream()
                .collect(standardDeviatingBigDecimal(BankAccount::getBalance));

        System.out.println("bigDecimalStatistics = " + bigDecimalStatistics);
        System.out.println("doubleStatistics = " + doubleStatistics);

        assertAll(
                () -> assertEquals(expected, standarDeviationBalances),
                () -> assertEquals(expectedStandardDeviationFromDouble, standarDeviationBalances)
        );
    }

    @Test
    void testStatisticsFromRandomGaussianDataset() {
        var targetMean = BigDecimal.valueOf(3);
        var targetStdDev = BigDecimal.valueOf(4);

        final var statistics = TestSampleGenerator
                .gaussianDoubles(100_000, targetMean.doubleValue(), targetStdDev.doubleValue())
                .mapToObj(BigDecimal::valueOf)
                .collect(BigDecimalCollectors.toBigDecimalStatistics());

        System.out.println("statistics = " + statistics);

        final var standardDeviation = statistics.getStandardDeviation()
                .setScale(1, RoundingMode.HALF_UP);
        final var average = statistics.getAverage()
                .setScale(1, RoundingMode.HALF_UP);

        assertAll(
                () -> assertEquals(targetStdDev.setScale(1, RoundingMode.HALF_UP), standardDeviation),
                () -> assertEquals(targetMean.setScale(1, RoundingMode.HALF_UP), average)
        );
    }

    @Test
    void testSummingBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        System.out.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(System.out::println);

        final var sumAsDouble = sampleBankAccountList.stream()
                .map(BankAccount::getBalance)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var sum = sampleBankAccountList.stream()
                .collect(summingBigDecimal(BankAccount::getBalance));

        System.out.println("sum = " + sum);

        final var expected = bigDecimalSummaryStatistics.getSum();

        assertAll(
                () -> assertEquals(sum, expected),
                () -> assertEquals(sumAsDouble, sum.doubleValue())
        );
    }

    @Test
    void testToMaxBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        System.out.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(System.out::println);

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var max = sampleBankAccountList.stream()
                .collect(toMaxBigDecimal(BankAccount::getBalance));

        System.out.println("max = " + max);

        final var expected = bigDecimalSummaryStatistics.getMax();
        assertEquals(max, expected);
    }

    @Test
    void testToMinBigDecimal() {
        final var sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        System.out.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(System.out::println);

        final var bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final var min = sampleBankAccountList.stream()
                .collect(toMinBigDecimal(BankAccount::getBalance));

        System.out.println("min = " + min);

        final var expected = bigDecimalSummaryStatistics.getMin();
        assertEquals(min, expected);
    }
}
