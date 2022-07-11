package hzt;

import hzt.model.Student;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static java.util.Comparator.comparingInt;
import static org.junit.jupiter.api.Assertions.*;

class ArrayTests {

    @Test
    void testSystemArrayCopyWithInts() {
        int[] sourceArray = {1, 5, 4, 7, 3, 8, 5, 6, 7, 3, 6};
        int[] destinationArray = new int[sourceArray.length];
        var destinationSubArray = new int[sourceArray.length];

        System.arraycopy(sourceArray, 0, destinationArray, 0, sourceArray.length);
        System.arraycopy(sourceArray, 0, destinationSubArray, 3, 6);

        Arrays.stream(destinationSubArray).forEach(System.out::println);

        assertAll(
                () -> assertArrayEquals(sourceArray, destinationArray),
                () -> assertArrayEquals(new int[]{0, 0, 0, 1, 5, 4, 7, 3, 8, 0, 0}, destinationSubArray)
        );
    }

    @Test
    void testSystemArrayCopyWithStudent() {
        Student[] sourceArray = {
                new Student(2, "Hans", 7),
                new Student(1, "Piet", 6)
        };
        Student[] destinationArray = new Student[sourceArray.length];

        System.arraycopy(sourceArray, 0, destinationArray, 0, sourceArray.length);

        assertArrayEquals(sourceArray, destinationArray);
    }

    //q22 test 4
    //Arrays.binarySearch() method returns the index of the search key, if it is contained in the list; otherwise,
    // (-(insertion point) - 1). The insertion point is defined as the point at which the key would be inserted into the list:
    // the index of the first element greater than the key, or list.size(),
    // if all elements in the list are less than the specified key.
    //
    // Note that this guarantees that the return value will be >= 0 if and only if the key is found.


    /**
     * Searches the specified array for the specified object using the binary search algorithm.
     * The array must be sorted into ascending order according to the specified comparator (as by the sort(T[], Comparator) method)
     * prior to making this call. If it is not sorted, the results are undefined.
     * <p>
     * If the array contains multiple elements equal to the specified object, there is no guarantee which one will be found.
     */
    @Test
    void testArraysBinarySearch() {
        String[] strings = {"d", "bbb", "aaaa"}; // array sorted in ascending order by string length
        final var compareByLength = comparingInt(String::length);

        final var indexOfStringLength1 = Arrays.binarySearch(strings, "x", compareByLength);
        final var indexOfStringLength2 = Arrays.binarySearch(strings, "ca", compareByLength);
        final var indexOfStringLength4 = Arrays.binarySearch(strings, "aaaa", compareByLength);

        Arrays.sort(strings);
        final var indexOfDWhenSortedInNaturalOrder = Arrays.binarySearch(strings, "d");

        assertAll(
                () -> assertEquals(-2, indexOfStringLength2), //(-(insertion point) - 1) = -1 - 1 = -2
                () -> assertEquals(0, indexOfStringLength1),
                () -> assertEquals(2, indexOfDWhenSortedInNaturalOrder),
                () -> assertEquals(2, indexOfStringLength4)
        );
    }

    //q26 test 4
    @Test
    void testJaggedArrayCreation() {
        int[][] a = new int[3][];
        a[0] = new int[2];
        a[1] = new int[4];
        a[2] = new int[100];

        assertAll(
                () -> assertDoesNotThrow(() -> a[2][90] = 10),
                () -> assertThrows(IndexOutOfBoundsException.class, () -> a[1][90] = 10),
                () -> assertEquals(2, a[0].length),
                () -> assertEquals(100, a[2].length)
        );
    }

    //q3 test 5
    @SuppressWarnings("CStyleArrayDeclaration")
    @Test
    void testDeclaringArrayInDifferentWays() {
        int[] a[] = new int[5][4];
        int b[][] = new int[5][4];
        int[][] d = new int[5][4];
        int[] c[] = new int[5][];

        for (int i = 0; i < c.length; i++) {
            c[i] = new int[4];
        }

        assertAll(
                () -> assertArrayEquals(a, b),
                () -> assertArrayEquals(b, c),
                () -> assertArrayEquals(c, d)
        );
    }

}
