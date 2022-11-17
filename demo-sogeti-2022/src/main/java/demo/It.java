package demo;

public final class It {

    private It() {
    }

    public static <T> T self(T t) {
        return t;
    }

    public static int asInt(int i) {
        return i;
    }

    public static int doubleAsInt(double d) {
        return (int) d;
    }

    public static int longAsInt(long l) {
        return (int) l;
    }

    public static long doubleAsLong(double d) {
        return (long) d;
    }

    public static long asLong(long l) {
        return l;
    }

    public static double asDouble(double d) {
        return d;
    }

    public static <T> boolean noFilter(@SuppressWarnings("unused") T t) {
        return true;
    }

    public static boolean noIntFilter(@SuppressWarnings("unused") int t) {
        return true;
    }

    public static boolean noLongFilter(@SuppressWarnings("unused") long t) {
        return true;
    }

    public static boolean noDoubleFilter(@SuppressWarnings("unused") double t) {
        return true;
    }

    public static <T> boolean blockingFilter(@SuppressWarnings("unused") T t) {
        return false;
    }

    public static void println() {
        System.out.println();
    }

    public static <T> void println(T value) {
        System.out.println(value);
    }

    public static <T> void print(T value) {
        System.out.print(value);
    }

    public static void printf(String format, Object... values) {
        System.out.printf(format, values);
    }

    public static <T> boolean notEquals(T t1, T t2) {
        return !t1.equals(t2);
    }
}
