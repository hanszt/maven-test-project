package hzt.collections;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toCollection;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <p>The twelve methods described above are summarized in the
 * following table:
 *
  <table class="striped">
  <caption>Summary of Deque methods</caption>
   <thead>
   <tr>
     <td rowspan="2"></td>
     <th scope="col" colspan="2"> First Element (Head)</th>
     <th scope="col" colspan="2"> Last Element (Tail)</th>
   </tr>
   <tr>
     <th scope="col" style="font-weight:normal; font-style:italic">Throws exception</th>
     <th scope="col" style="font-weight:normal; font-style:italic">Special value</th>
     <th scope="col" style="font-weight:normal; font-style:italic">Throws exception</th>
     <th scope="col" style="font-weight:normal; font-style:italic">Special value</th>
   </tr>
   </thead>
   <tbody>
   <tr>
     <th scope="row">Insert</th>
     <td>{@link java.util.Deque#addFirst(Object) addFirst(e)}</td>
     <td>{@link java.util.Deque#offerFirst(Object) offerFirst(e)}</td>
     <td>{@link java.util.Deque#addLast(Object) addLast(e)}</td>
     <td>{@link java.util.Deque#offerLast(Object) offerLast(e)}</td>
   </tr>
   <tr>
     <th scope="row">Remove</th>
     <td>{@link java.util.Deque#removeFirst() removeFirst()}</td>
     <td>{@link java.util.Deque#pollFirst() pollFirst()}</td>
     <td>{@link java.util.Deque#removeLast() removeLast()}</td>
     <td>{@link java.util.Deque#pollLast() pollLast()}</td>
   </tr>
   <tr>
     <th scope="row">Examine</th>
     <td>{@link java.util.Deque#getFirst() getFirst()}</td>
     <td>{@link java.util.Deque#peekFirst() peekFirst()}</td>
     <td>{@link java.util.Deque#getLast() getLast()}</td>
     <td>{@link java.util.Deque#peekLast() peekLast()}</td>
   </tr>
   </tbody>
  </table>
 */
class DequeTest {

    @Test
    void testAllMethodsInsertingElementAtHeadOfDeque() {
        final Integer[] expected = {3, 2, 1, 0};

        Deque<Integer> deque = new ArrayDeque<>(List.of(0));
        assertTrue(deque.offerFirst(1));
        assertDoesNotThrow(() -> deque.addFirst(2));
        assertDoesNotThrow(() -> deque.push(3));

        deque.forEach(System.out::println);

        assertArrayEquals(expected, deque.toArray(Integer[]::new));
    }

    /**
     * Behaviour of queue
     */
    @Test
    void testAllMethodsInsertingElementAtTailOfDeque() {
        final Integer[] expected = {0, 1, 2, 3, 4};

        Deque<Integer> deque = new ArrayDeque<>(List.of(0));
        assertDoesNotThrow(() -> deque.addLast(1));
        assertTrue(deque.add(2));
        assertTrue(deque.offerLast(3));
        assertTrue(deque.offer(4));

        deque.forEach(System.out::println);

        Queue<Integer> queue = new ArrayDeque<>(List.of(0));
        assertTrue(queue.offer(1));
        assertTrue(queue.add(2));
        assertTrue(queue.add(3));
        assertTrue(queue.offer(4));

        Stack<Integer> stack = new Stack<>();
        assertEquals(0, stack.push(0));
        assertEquals(1, stack.push(1));
        assertEquals(2, stack.push(2));
        assertEquals(3, stack.push(3));
        assertEquals(4, stack.push(4));

        assertArrayEquals(expected, deque.toArray(Integer[]::new));
        assertArrayEquals(expected, queue.toArray(Integer[]::new));
        assertArrayEquals(expected, stack.toArray(Integer[]::new));
    }

    /**
     * Behaviour of queue
     */
    @Test
    void testAllMethodsRemovingElementAtHeadOfDeque() {
        final Integer[] expected = {6, 7, 8, 9, 10};

        Deque<Integer> deque = createDeque(1, 10);

        assertEquals(1, deque.remove());
        assertEquals(2, deque.removeFirst());
        assertEquals(3, deque.pop());
        assertEquals(4, deque.pollFirst());
        assertEquals(5, deque.poll());

        Queue<Integer> queue = createDeque(4, 10);

        assertEquals(4, queue.remove());
        assertEquals(5, queue.poll());

        deque.forEach(System.out::println);

        assertArrayEquals(expected, deque.toArray(Integer[]::new));
        assertArrayEquals(expected, queue.toArray(Integer[]::new));
    }

    /**
     * Behaviour of Stack
     */
    @Test
    void testAllMethodsRemovingElementAtTailOfDeque() {
        final Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8};

        Deque<Integer> deque = createDeque(1, 10);

        assertEquals(10, deque.removeLast());
        assertEquals(9, deque.pollLast());

        Stack<Integer> stack = createDeque(1, 9).stream()
                .collect(toCollection(Stack::new));

        assertEquals(9, stack.pop());

        deque.forEach(System.out::println);

        assertArrayEquals(expected, deque.toArray(Integer[]::new));
        assertArrayEquals(expected, stack.toArray(Integer[]::new));
    }

    /**
     * Behaviour of queue
     */
    @Test
    void testAllMethodsExaminingElementAtHeadOfDeque() {
        final Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        Deque<Integer> deque = createDeque(1, 10);

        assertEquals(1, deque.peekFirst());
        assertEquals(1, deque.getFirst());
        assertEquals(1, deque.peek());
        assertEquals(1, deque.element());

        Queue<Integer> queue = createDeque(1, 10);

        assertEquals(1, queue.peek());
        assertEquals(1, queue.element());

        deque.forEach(System.out::println);

        assertArrayEquals(expected, deque.toArray(Integer[]::new));
        assertArrayEquals(expected, queue.toArray(Integer[]::new));
    }

    /**
     * Behaviour of Stack
     */
    @Test
    void testAllMethodsExaminingElementAtTailOfDeque() {
        final Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        Deque<Integer> deque = createDeque(1, 10);

        assertEquals(10, deque.peekLast());
        assertEquals(10, deque.getLast());

        Stack<Integer> stack = createDeque(1, 10).stream()
                .collect(toCollection(Stack::new));
        assertEquals(10, stack.peek());

        deque.forEach(System.out::println);

        assertArrayEquals(expected, deque.toArray(Integer[]::new));
        assertArrayEquals(expected, stack.toArray(Integer[]::new));
    }

    @Test
    void testDequeueMethodsRandom() {
        Deque<Integer> d = new ArrayDeque<>();
        d.push(1);
        d.push(2);
        d.push(3);

        final var v1 = d.pollFirst();
        final var v2 = d.poll();
        final var v3 = d.pollLast();

        System.out.println(v1);
        System.out.println(v2);
        System.out.println(v3);

        assertEquals(3, v1);
        assertEquals(2, v2);
        assertEquals(1, v3);
    }

    private static Deque<Integer> createDeque(int startInclusive, int endInclusive) {
        return IntStream.rangeClosed(startInclusive, endInclusive)
                .boxed()
                .collect(toCollection(ArrayDeque::new));
    }
}
