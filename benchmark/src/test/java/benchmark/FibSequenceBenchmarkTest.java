package benchmark;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FibSequenceBenchmarkTest {

    private final FibSequenceBenchmark fibSequenceBenchmark = new FibSequenceBenchmark();

    @Test
    void testDoubleListSortAndListSortYieldSameContent() {
        final var fibonacciSequenceToList = fibSequenceBenchmark.bigIntFibonacciSequenceToList();
        final var fibonacciStreamToList = fibSequenceBenchmark.bigIntFibonacciStreamToList();
        final var fibonacciStreamUsingRecordToList = fibSequenceBenchmark.bigIntFibonacciStreamUsingRecordToList();

        assertAll(
                () -> assertEquals(fibonacciSequenceToList, fibonacciStreamToList),
                () -> assertEquals(fibonacciSequenceToList, fibonacciStreamUsingRecordToList)
        );
    }
}
