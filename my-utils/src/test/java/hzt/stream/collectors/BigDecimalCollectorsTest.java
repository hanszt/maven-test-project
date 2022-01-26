package hzt.stream.collectors;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.BankAccount;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static hzt.stream.collectors.BigDecimalCollectors.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BigDecimalCollectorsTest {

    @Test
    void testSummarizingBigDecimal() {
        final List<BankAccount> sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        System.out.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(System.out::println);

        final BigDecimalSummaryStatistics bigDecimalSummaryStatistics = sampleBankAccountList.stream()
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
        final List<BankAccount> sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        System.out.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(System.out::println);

        final BigDecimalSummaryStatistics bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final BigDecimal average = sampleBankAccountList.stream()
                .collect(averagingBigDecimal(BankAccount::getBalance));

        System.out.println("average = " + average);

        final BigDecimal expected = bigDecimalSummaryStatistics.getAverage();
        assertEquals(average, expected);
    }

    @Test
    @Disabled("Standard deviation not implemented")
    void testStandardDeviatingBigDecimal() {
        final List<BankAccount> sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        System.out.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(System.out::println);

        final DoubleStatistics doubleStatistics = sampleBankAccountList.stream()
                .map(BankAccount::getBalance)
                .collect(CollectorsX.toDoubleStatisticsBy(BigDecimal::doubleValue));

        final BigDecimal expectedStandardDeviationFromDouble = BigDecimal.valueOf(doubleStatistics.getStandardDeviation())
                .setScale(2, RoundingMode.HALF_UP);

        final BigDecimalStatistics bigDecimalStatistics = sampleBankAccountList.stream()
                .collect(toBigDecimalStatisticsBy(BankAccount::getBalance));
        final BigDecimal expected = bigDecimalStatistics.getStandardDeviation();

        final BigDecimal standarDeviationBalances = sampleBankAccountList.stream()
                .collect(standardDeviatingBigDecimal(BankAccount::getBalance));

        System.out.println("bigDecimalStatistics = " + bigDecimalStatistics);
        System.out.println("doubleStatistics = " + doubleStatistics);

        assertAll(
                () -> assertEquals(expected, standarDeviationBalances),
                () -> assertEquals(expectedStandardDeviationFromDouble, standarDeviationBalances)
        );
    }

    @Test
    @Disabled("Standard deviation not implemented")
    void testStatisticsFromRandomGaussianDataset() {
        BigDecimal targetMean = BigDecimal.valueOf(3);
        BigDecimal targetStdDev = BigDecimal.valueOf(4);

        final BigDecimalStatistics statistics = TestSampleGenerator
                .gaussianDoubles(100_000, targetMean.doubleValue(), targetStdDev.doubleValue())
                .mapToObj(BigDecimal::valueOf)
                .collect(BigDecimalCollectors.toBigDecimalStatistics());

        System.out.println("statistics = " + statistics);

        final BigDecimal standardDeviation = statistics.getStandardDeviation()
                .setScale(1, RoundingMode.HALF_UP);
        final BigDecimal average = statistics.getAverage()
                .setScale(1, RoundingMode.HALF_UP);

        assertAll(
                () -> assertEquals(targetStdDev.setScale(1, RoundingMode.HALF_UP), standardDeviation),
                () -> assertEquals(targetMean.setScale(1, RoundingMode.HALF_UP), average)
        );
    }

    @Test
    void testSummingBigDecimal() {
        final List<BankAccount> sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        System.out.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(System.out::println);

        final double sumAsDouble = sampleBankAccountList.stream()
                .map(BankAccount::getBalance)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();

        final BigDecimalSummaryStatistics bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final BigDecimal sum = sampleBankAccountList.stream()
                .collect(summingBigDecimal(BankAccount::getBalance));

        System.out.println("sum = " + sum);

        final BigDecimal expected = bigDecimalSummaryStatistics.getSum();

        assertAll(
                () -> assertEquals(sum, expected),
                () -> assertEquals(sumAsDouble, sum.doubleValue())
        );
    }

    @Test
    void testToMaxBigDecimal() {
        final List<BankAccount> sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        System.out.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(System.out::println);

        final BigDecimalSummaryStatistics bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final BigDecimal max = sampleBankAccountList.stream()
                .collect(toMaxBigDecimal(BankAccount::getBalance));

        System.out.println("max = " + max);

        final BigDecimal expected = bigDecimalSummaryStatistics.getMax();
        assertEquals(max, expected);
    }

    @Test
    void testToMinBigDecimal() {
        final List<BankAccount> sampleBankAccountList = TestSampleGenerator.createSampleBankAccountList();
        System.out.println("Sample bankaccountList:");
        sampleBankAccountList.forEach(System.out::println);

        final BigDecimalSummaryStatistics bigDecimalSummaryStatistics = sampleBankAccountList.stream()
                .collect(summarizingBigDecimal(BankAccount::getBalance));

        final BigDecimal min = sampleBankAccountList.stream()
                .collect(toMinBigDecimal(BankAccount::getBalance));

        System.out.println("min = " + min);

        final BigDecimal expected = bigDecimalSummaryStatistics.getMin();
        assertEquals(min, expected);
    }
}
