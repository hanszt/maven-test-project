package hzt.stream.collectors;

import java.util.Objects;

public final class TriTuple<R1, R2, R3> {
    private final R1 first;
    private final R2 second;
    private final R3 third;

    TriTuple(R1 first, R2 second, R3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public R1 first() {
        return first;
    }

    public R2 second() {
        return second;
    }

    public R3 third() {
        return third;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof TriTuple<?, ?, ?> that) {
            return Objects.equals(this.first, that.first) &&
                    Objects.equals(this.second, that.second) &&
                    Objects.equals(this.third, that.third);
        }
        return false;
    }


    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "TriTuple[" +
                "first=" + first + ", " +
                "second=" + second + ", " +
                "third=" + third + ']';
    }

}
