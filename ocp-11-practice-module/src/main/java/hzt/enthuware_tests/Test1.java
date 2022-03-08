package hzt.enthuware_tests;

import java.util.Collections;
import java.util.List;

public final class Test1 {

    private Test1() {
    }

    public static void listOfAndSetOfDoNotSupportNullElements() {
        var numA = new Integer[]{1, null, 3}; //1
        var list1 = List.of(numA); //2
        var list2 = Collections.unmodifiableList(list1); //3
        numA[1] = 2; //4
        System.out.println(list1 + " " + list2);
    }

    // Source: Enthuware Java11OCP Test 1 Q 36
    static int calculateSum(int[] values) {
        int sum = 0;
        int i = 0;
        try {
            while (values[i] < 100) {
                sum = sum + values[i];
                i++;
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return sum;
    }
}
