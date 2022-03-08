package hzt.service_provider_modules;

import hzt.interfaces.FunctionalInterfaceContainingMoreThanOneAbstractMethod;

public class Adder implements FunctionalInterfaceContainingMoreThanOneAbstractMethod {

    public static final String NAME = "Hans";

    private final int value;

    public Adder() {
        this.value = 0;
    }

    public Adder(int value) {
        this.value = value;
    }

    @Override
    public int apply(int valueToAdd) {
        return value + valueToAdd;
    }

    public static int add(int a, int b) {
        return a + b;
    }
}
