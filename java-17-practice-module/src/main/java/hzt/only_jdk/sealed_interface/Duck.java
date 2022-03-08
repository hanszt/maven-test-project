package hzt.only_jdk.sealed_interface;

public final class Duck implements Quackable {

    @Override
    @SafeVarargs
    public final <T> void addQuacks(T... args) {
        // it is a proof of concept
    }

    @SafeVarargs
    public final <T> void addFood(T... args) {
        // a proof of concept
    }
}
