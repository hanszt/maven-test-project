package hzt.behavioural_patterns.interpreter_pattern;

import java.util.Map;

class Number implements Operand {
    private final double value;

    public Number(double value) {
        this.value = value;
    }

    @Override
    public void traverse(int level) {
        System.out.print(value + " ");
    }

    @Override
    public double evaluate(Map<String, Integer> context) {
        return value;
    }
}
