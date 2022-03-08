package hzt.concurrency;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AtomicTests {

    // Q 12 test 3
    @Test
    void testAtomicIntegerIncrement() {
        var atomicInteger = new AtomicInteger();
        assertEquals(1, atomicInteger.incrementAndGet());
        assertEquals(6, atomicInteger.addAndGet(5));
    }

    @Test
    void testAtomicIntegerArray() {
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[] {1, 3, 5, 6});
        atomicIntegerArray.addAndGet(3, 1);
        atomicIntegerArray.incrementAndGet(2);
        assertEquals(7, atomicIntegerArray.get(3));
        assertEquals(6, atomicIntegerArray.get(2));
    }
}
