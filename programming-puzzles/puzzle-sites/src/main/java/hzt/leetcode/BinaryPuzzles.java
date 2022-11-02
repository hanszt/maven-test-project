package hzt.leetcode;

import org.jetbrains.annotations.NotNull;

public final class BinaryPuzzles {

    private BinaryPuzzles() {
    }

    /**
     * Given the binary representation of an integer as a string s, return the number of steps to reduce it to 1 under the following rules:
     * <p>
     * If the current number is even, you have to divide it by 2.
     * <p>
     * If the current number is odd, you have to add 1 to it.
     * <p>
     * It is guaranteed that you can always reach one for all test cases.
     * <p>
     * <a href="https://leetcode.com/problems/number-of-steps-to-reduce-a-number-in-binary-representation-to-one/">
     * 1404. Number of Steps to Reduce a Number in Binary Representation to One</a>
     *
     * @param s the binary representation of a nr
     * @return nr of steps to reduce to one
     * <p>
     * Accepted	22 ms	47.1 MB
     */
    public static int numStepsToReduceToOne(final String s) {
        if ("1".equals(s)) {
            return 0;
        }
        var numSteps = 0;
        String reduction = s;
        while (!"1".equals(reduction)) {
            final var isEven = reduction.charAt(reduction.length() - 1) == '0';
            reduction = isEven ? divideBinaryBy2(reduction) : addBinary(reduction, "1");
            numSteps++;
        }
        return numSteps;
    }

    @NotNull
    private static String divideBinaryBy2(final String s) {
        return s.substring(0, s.length() - 1);
    }


    /**
     * <a href="https://leetcode.com/problems/add-binary/">67. Add Binary</a>
     *
     * @param a binary nr one
     * @param b binary nr two
     * @return the sum as binary nr
     */
    public static String addBinary(final String a, final String b) {
        int i = a.length() - 1;
        int j = b.length() - 1;
        int carry = 0;

        final var sb = new StringBuilder();
        while (i >= 0 || j >= 0) {
            int sum = carry;
            if (i >= 0) {
                sum += (a.charAt(i--) - '0');
            }
            if (j >= 0) {
                sum += (b.charAt(j--) - '0');
            }
            sb.append(sum % 2);
            carry = sum / 2;
        }
        if (carry != 0) {
            sb.append(carry);
        }
        return sb.reverse().toString();
    }
}
