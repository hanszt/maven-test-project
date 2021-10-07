package hzt.stream.utils;

import java.util.Objects;

public final class MyObjects {

    private MyObjects() {
    }

    @SafeVarargs
    public static <T> void requireAllNonNull(final Class<T> aClass, T... objects) {
        int counter = 1;
        for (T t : objects) {
            Objects.requireNonNull(t, aClass.getSimpleName() + " " + counter + " is null");
            counter++;
        }
    }
}
