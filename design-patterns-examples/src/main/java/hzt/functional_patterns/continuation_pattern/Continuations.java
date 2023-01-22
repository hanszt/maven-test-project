package hzt.functional_patterns.continuation_pattern;

import java.math.BigInteger;
import java.util.function.Consumer;

/**
 * @see <a href="https://blog.marcinchwedczuk.pl/continuations-in-java">Continuations in Java</a>
 */
public final class Continuations {

    private Continuations() {
    }

    public static Thunk addInts(int a, int b, Continuation<Integer> continuation) {
        return continuation.apply(a + b);
    }

    public static Thunk addLongs(long a, long b, Continuation<Long> continuation) {
        return continuation.apply(a + b);
    }

    public static Thunk addBigInts(BigInteger a, BigInteger b, Continuation<BigInteger> continuation) {
        return continuation.apply(a.add(b));
    }

    public static Thunk multiplyInts(int a, int b, Continuation<Integer> continuation) {
        return continuation.apply(a * b);
    }

    public static Thunk multiplyLongs(long a, long b, Continuation<Long> continuation) {
        return continuation.apply(a * b);
    }

    public static Thunk multiplyBigInts(BigInteger a, BigInteger b, Continuation<BigInteger> continuation) {
        return continuation.apply(a.multiply(b));
    }

    public static Thunk iff(boolean expr,
                            Continuation<Boolean> trueBranch,
                            Continuation<Boolean> falseBranch) {
        if (expr) {
            return trueBranch.apply(true);
        } else {
            return falseBranch.apply(false);
        }
    }

    public static Thunk sumCC(long from, long to, Continuation<Long> cont) {
        return longsGreaterThan(from, to, fromGreaterThanTo ->
                iff(fromGreaterThanTo,
                        x -> cont.apply(0L),
                        x -> addLongs(from, 1, from1 ->
                                sumCC(from1, to, sumCC ->
                                        addLongs(from, sumCC, cont)))));
    }

    public static Thunk intsGreaterThan(int one, int other, Continuation<Boolean> continuation) {
        return continuation.apply(one > other);
    }

    public static Thunk eq(int one, int other, Continuation<Boolean> continuation) {
        return continuation.apply(one == other);
    }

    public static <T> Thunk eq(T one, T other, Continuation<Boolean> continuation) {
        return continuation.apply(one.equals(other));
    }

    public static Thunk longsGreaterThan(long one, long other, Continuation<Boolean> continuation) {
        return continuation.apply(one > other);
    }

    public static Thunk factorial(int n, Continuation<Integer> cont) {
        return eq(n, 0, isNZero ->
                iff(isNZero,
                        x -> cont.apply(1),
                        x -> addInts(n, -1, nm1 ->
                                factorial(nm1, fnm1 ->
                                        multiplyInts(n, fnm1, cont)))));
    }

    public static Thunk bigIntFactorial(BigInteger n, Continuation<BigInteger> cont) {
        return eq(n, BigInteger.ZERO, isNZero ->
                iff(isNZero,
                        x -> cont.apply(BigInteger.ONE),
                        x -> addBigInts(n, BigInteger.valueOf(-1), nm1 ->
                                bigIntFactorial(nm1, fnm1 -> multiplyBigInts(n, fnm1, cont)))));
    }

    public static Thunk trampolineBigIntFactorial(BigInteger n, Continuation<BigInteger> cont) {
        return () -> eq(n, BigInteger.ZERO, isNZero ->
                () -> iff(isNZero,
                        x -> () -> cont.apply(BigInteger.ONE),
                        x -> () -> addBigInts(n, BigInteger.valueOf(-1), nm1 ->
                                bigIntFactorial(nm1, fnm1 -> () -> multiplyBigInts(n, fnm1, cont)))));
    }

    public static Thunk fib(int n, Continuation<Integer> cont) {
        return intsGreaterThan(n, 1, nlt2 ->
                iff(nlt2,
                        x -> addInts(n, -1, nm1 ->
                                fib(nm1, fnm1 ->
                                        addInts(n, -2, nm2 ->
                                                fib(nm2, fnm2 -> addInts(fnm1, fnm2, cont))))),
                        x -> cont.apply(1)));
    }

    static Thunk add(int a, int b, Continuation<Integer> cont) {
        int sum = a + b;
        return cont.apply(sum);
    }

    public static void trampoline(Thunk initThunk) {
        Thunk thunk = initThunk;
        while (thunk != null) {
            thunk = thunk.get();
        }
    }

    static <T> Continuation<T> endCall(Consumer<T> call) {
        return r -> {
            call.accept(r);
            return null;
        };
    }
}
