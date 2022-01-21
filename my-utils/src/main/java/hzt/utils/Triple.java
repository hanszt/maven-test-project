package hzt.utils;

import java.util.Objects;

public final class Triple<A, B, C> {
    private final A first;
    private final B second;
    private final C third;

    public Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public A first() {
        return first;
    }

    public B second() {
        return second;
    }

    public C third() {
        return third;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Triple) obj;
        return Objects.equals(this.first, that.first) &&
                Objects.equals(this.second, that.second) &&
                Objects.equals(this.third, that.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "Triple[" +
                "first=" + first + ", " +
                "second=" + second + ", " +
                "third=" + third + ']';
    }

}
