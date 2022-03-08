package hzt.model;

import java.util.function.UnaryOperator;

public enum NumberEnum {

    ONE(1, i -> i) {
        @Override
        public int add(int toAdd) {
            return getValue() + toAdd;
        }
    }, TWO(2, i -> multiply(i, 2)) {
        @Override
        public int add(int toAdd) {
            return 2 + toAdd;
        }
    }, THREE(3, i -> i * 3) {
        @Override
        public int add(int toAdd) {
            return 3 + toAdd;
        }
    }, FOUR(4, i -> i * 4) {
        @Override
        public int add(int toAdd) {
            return 4 + toAdd;
        }

        @Override
        public String description() {
            return name();
        }
    };

    private final int value;
    private final UnaryOperator<Integer> multiplier;

    NumberEnum(int value, UnaryOperator<Integer> multiplier) {
        this.value = value;
        this.multiplier = multiplier;
    }

    public abstract int add(int toAdd);

    public int getValue() {
        return value;
    }

    public UnaryOperator<Integer> getMultiplier() {
        return multiplier;
    }

    public String description() {
        return name() + " An enum test";
    }

    private static int multiply(int x, int y) {
        return x * y;
    }
}
