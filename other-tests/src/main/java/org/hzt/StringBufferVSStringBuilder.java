package org.hzt;

import static org.hzt.utils.It.println;

/**
 * Run this program first couple of times. We see that the StringBuilder does not
 * give us reliable results because its methods are not thread-safe as compared
 * to StringBuffer.
 * <p>
 * For example, the single append in StringBuffer is thread-safe, i.e.
 * only one thread can call append() at any time and would finish writing
 * back to memory one at first time. In contrast, the append() in the StringBuilder
 * class can be called concurrently by many threads, so the final size of the
 * StringBuilder is sometimes less than expected.
 */
@SuppressWarnings("squid:S1149")
public class StringBufferVSStringBuilder {

    public static void main(String... args) {
        try {
            testStringBufferVsStringBuilder();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }

    private static void testStringBufferVsStringBuilder() throws InterruptedException {
        int threadAmount = 100;

        //*************************String Builder Test*******************************//
        StringBuilder sb = new StringBuilder();
        StringBuilderTest[] builderThreads = new StringBuilderTest[threadAmount];
        for (int i = 0; i < threadAmount; i++) {
            builderThreads[i] = new StringBuilderTest(sb);
        }
        for (int i = 0; i < threadAmount; i++) {
            builderThreads[i].start();
        }
        for (int i = 0; i < threadAmount; i++) {
            builderThreads[i].join();
        }
        println("StringBuilderTest: Expected result is 1000; got " + sb.length());

        //*************************String Buffer Test*******************************//

        StringBuffer sb2 = new StringBuffer();
        StringBufferTest[] bufferThreads = new StringBufferTest[threadAmount];
        for (int i = 0; i < threadAmount; i++) {
            bufferThreads[i] = new StringBufferTest(sb2);
        }
        for (int i = 0; i < threadAmount; i++) {
            bufferThreads[i].start();
        }
        for (int i = 0; i < threadAmount; i++) {
            bufferThreads[i].join();
        }
        println("StringBufferTest: Expected result is 1000; got " + sb2.length());
    }


    // Every run would attempt to append 100 "A"s to the StringBuilder.
    private static final class StringBuilderTest extends Thread {

        StringBuilder sb;

        public StringBuilderTest(StringBuilder sb) {
            this.sb = sb;
        }

        @Override
        public void run() {
            sb.append("A".repeat(100));

        }
    }


    //Every run would attempt to append 100 "A"s to the StringBuffer.
    private static final class StringBufferTest extends Thread {

        StringBuffer sb2;

        public StringBufferTest(StringBuffer sb2) {
            this.sb2 = sb2;
        }

        @Override
        public void run() {
            sb2.append("A".repeat(100));
        }
    }
}
