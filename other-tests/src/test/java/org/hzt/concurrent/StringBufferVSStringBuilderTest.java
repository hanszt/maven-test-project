package org.hzt.concurrent;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Run this program first couple of times. We see that the StringBuilder does not
 * give us reliable results because its methods are not thread-safe as compared
 * to StringBuffer.
 * <p>
 * For example, the single append in StringBuffer is thread-safe, i.e.
 * only one thread can call append() at any time and would finish writing
 * back to memory one at first time. In contrast, the 'append()' in the StringBuilder
 * class can be called concurrently by many threads, so the final size of the
 * StringBuilder is sometimes less than expected.
 * <a href="https://stackoverflow.com/questions/355089/difference-between-stringbuilder-and-stringbuffer">Difference between StringBuilder and StringBuffer</a>
 */
class StringBufferVSStringBuilderTest {

    @Test
    void testStringBuilderVsStringBuffer() {
        //noinspection StringBufferMayBeStringBuilder
        final var stringBuffer = new StringBuffer();
        consumeInt1000TimesIn10Threads(stringBuffer::append);
        final var bufferLength = stringBuffer.length();
        println("bufferLength = " + bufferLength);

        final var stringBuilder = new StringBuilder();
        consumeInt1000TimesIn10Threads(stringBuilder::append);
        final var builderLength = stringBuilder.length();
        println("builderLength = " + builderLength);

        final var expected = 10_000;

        assertEquals(expected, bufferLength);
    }


    /**
     * @param consumer the consumer that consumes the exposed ints
     * @see <a href="https://www.baeldung.com/java-thread-join">The Thread.join() Method in Java</a>
     */
    private static void consumeInt1000TimesIn10Threads(IntConsumer consumer) {
        final var threads = IntStream.range(0, 10)
                .mapToObj(i -> new Thread(() -> IntStream.range(0, 1_000)
                        .map(j -> j % 10)
                        .forEach(consumer)))
                .toArray(Thread[]::new);

        Arrays.stream(threads).forEach(Thread::start);

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
