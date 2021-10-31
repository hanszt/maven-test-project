package hzt.stream.collectors;

import java.util.Objects;

public final class QuadTuple<R1, R2, R3, R4> {
    private final R1 first;
    private final R2 second;
    private final R3 third;
    private final R4 fourth;

    QuadTuple(R1 first, R2 second, R3 third, R4 fourth) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.fourth = fourth;
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

    public R4 fourth() {
        return fourth;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this || (obj instanceof QuadTuple<?, ?, ?, ?> that &&
                Objects.equals(this.first, that.first) &&
                Objects.equals(this.second, that.second) &&
                Objects.equals(this.third, that.third) &&
                Objects.equals(this.fourth, that.fourth));
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third, fourth);
    }

    @Override
    public String toString() {
        return "QuadTuple[" +
                "first=" + first + ", " +
                "second=" + second + ", " +
                "third=" + third + ", " +
                "fourth=" + fourth + ']';
    }

}
