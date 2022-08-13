package org.hzt.collections;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FibonacciHeapTest {

    @Test
    void testFibonacciHeap() {
        FibonacciHeap<String> fibonacciHeap = new FibonacciHeap<>();
        fibonacciHeap.enqueue("Hello", 4);
        fibonacciHeap.enqueue("Hello", 3);
        fibonacciHeap.enqueue("Bye", 7);
        assertEquals(3, fibonacciHeap.size());
    }

}
