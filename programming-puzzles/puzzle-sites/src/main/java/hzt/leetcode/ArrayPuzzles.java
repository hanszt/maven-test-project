package hzt.leetcode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Stream;

public final class ArrayPuzzles {

    private ArrayPuzzles() {
    }

    /**
     * Given an array of integers nums and an integer k,
     * return the number of contiguous sub-arrays where the product of all the elements in the subarray is strictly less than k.
     *
     * @return the nr of sub-arrays strictly less than k
     * @see <a href="https://leetcode.com/problems/subarray-product-less-than-k/">713. Subarray Product Less Than K</a>
     * <p>
     * Accepted	1275 ms	67.8 MB	java
     */
    public static int subArrayProductLessThanNrK(final int[] nums, final int k) {
        // the default case made it pass
        if (k <= 1) {
            return 0;
        }
        int productLessThanKCount = 0;
        for (int startIndex = 0; startIndex < nums.length; startIndex++) {
            int product = 1;
            for (int i = startIndex; i < nums.length; i++) {
                product *= nums[i];
                if (product >= k) {
                    break;
                }
                productLessThanKCount++;
            }
        }
        return productLessThanKCount;
    }

    /**
     * Solution by javachip8786
     * <p>
     * Faster than my solution.
     * <p>
     * Accepted 7 ms	67.7 MB	java
     */
    public static int numSubarrayProductLessThanKFast(final int[] nums, final int k) {
        if (k <= 1) {
            return 0;
        }
        int product = 1;
        int result = 0;

        int left = 0;
        int right = 0;
        while (right < nums.length) {
            product *= nums[right];
            //if we reach the limit, divide until becomes valid again
            while (product >= k) {
                product /= nums[left];
                left++;
            }
            result += right - left + 1;
            right++;
        }
        return result;
    }

    /**
     * 462. Minimum Moves to Equal Array Elements II
     * <p>
     * Given an integer array nums of size n, return the minimum number of moves required to make all array elements equal.
     * <p>
     * In one move, you can increment or decrement an element of the array by 1.
     * <p>
     * Test cases are designed so that the answer will fit in a 32-bit integer.
     * <p>
     * Example 1:
     * <p>
     * Input: nums = [1,2,3]
     * Output: 2
     * Explanation:
     * Only two moves are needed (remember each move increments or decrements one element):
     * [1,2,3]  =>  [2,2,3]  =>  [2,2,2]
     *
     * @param nums the nrs to search through
     * @return the minimum nr of moves to get to an equal element
     * <p>
     * Accepted	7 ms	45.9 MB
     */
    public static int minMoves2(final int[] nums) {
        if (nums.length == 1) {
            return 0;
        }
        if (nums.length == 2) {
            return Math.abs(nums[0] - nums[1]);
        }
        Arrays.sort(nums);
        final int median = nums[nums.length / 2];
        int nrOfMoves = 0;
        for (final int num : nums) {
            nrOfMoves += Math.abs(num - median);
        }
        return nrOfMoves;
    }

    /**
     * You are given a 2D integer array orders, where each orders[i] = [price-i, amount-i, orderType-i] denotes that amount-i
     * orders have been placed of type orderType-i at the price price-i. The orderType-i is:
     * <p>
     * 0 if it is a batch of buy orders, or
     * 1 if it is a batch of sell orders.
     * Note that orders[i] represents a batch of amount-i independent orders with the same price and order type.
     * All orders represented by orders[i] will be placed before all orders represented by orders[i+1] for all valid i.
     * <p>
     * There is a backlog that consists of orders that have not been executed. The backlog is initially empty. When an order is placed,
     * the following happens:
     * <p>
     * If the order is a buy order, you look at the sell order with the smallest price in the backlog.
     * If that sell order's price is smaller than or equal to the current buy order's price, they will match and be executed,
     * and that sell order will be removed from the backlog. Else, the buy order is added to the backlog.
     * Vice versa, if the order is a sell order, you look at the buy order with the largest price in the backlog.
     * If that buy order's price is larger than or equal to the current sell order's price, they will match and be executed,
     * and that buy order will be removed from the backlog. Else, the sell order is added to the backlog.
     * <p>
     * Return the total amount of orders in the backlog after placing all the orders from the input. Since this number can be large,
     * return it modulo 109 + 7.
     * <p>
     * Constraints:
     * <p>
     * 1 <= orders.length <= 105
     * orders[i].length == 3
     * 1 <= price-i, amount-i <= 109
     * orderType-i is either 0 or 1.
     *
     * @param orders the orders containing orders[i] = [price-i, amount-i, orderType-i]
     * @return nr of orders as modulo 109 + 7
     * <p>
     * Credits to: xecute
     *
     * @see <a href="https://leetcode.com/problems/number-of-orders-in-the-backlog/">801. Number of Orders in the Backlog</a>
     *
     * Accepted	49 ms	79.6 MB
     */
    public static int getNumberOfBacklogOrders(final int[][] orders) {
        final Comparator<Order> compareByOrderPrice = Comparator.comparingInt(order -> order.price);
        final Queue<Order> sells = new PriorityQueue<>(compareByOrderPrice);
        final Queue<Order> buys = new PriorityQueue<>(compareByOrderPrice.reversed());
        final int BUY = 0;
        final int SELL = 1;
        for (final int[] o : orders) {
            final Order order = new Order(o[0], o[1], o[2]);
            if (order.type == BUY) {
                buys.add(order);
            }
            if (order.type == SELL) {
                sells.add(order);
            }
            match(buys, sells);
        }
        final int MOD = 1_000_000_000 + 7;
        return Stream.concat(buys.stream(), sells.stream())
                .mapToInt(o -> o.amount)
                .reduce(0, (total, amount) -> (total + amount) % MOD);
    }

    private static void match(final Queue<Order> buys, final Queue<Order> sells) {
        while (!buys.isEmpty() && !sells.isEmpty()) {
            final Order buy = buys.peek();
            final Order sell = sells.peek();
            if (sell.price > buy.price) {
                break;
            }
            final int sellAmount = sell.amount;
            final int buyAmount = buy.amount;
            sell.amount -= buyAmount;
            buy.amount -= sellAmount;
            if (buy.amount <= 0) {
                buys.poll();
            }
            if (sell.amount <= 0) {
                sells.poll();
            }
        }
    }

    private static class Order {
        private final int price;
        private int amount;
        private final int type;

        public Order(final int p, final int a, final int t) {
            price = p;
            amount = a;
            type = t;
        }
    }
}
