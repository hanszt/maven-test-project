package hzt.behavioural_patterns.interpreter_pattern;

import java.util.Map;

class Expression implements Operand {

    private final char operation;
    private Operand left;
    private Operand right;

    public Expression(char operation) {
        this.operation = operation;
    }

    public void traverse(int level) {
        getLeft().traverse(level + 1);
        System.out.print("" + level + operation + level + " ");
        getRight().traverse(level + 1);
    }

    public double evaluate(Map<String, Integer> context) {
        double result = 0;
        double a = getLeft().evaluate(context);
        double b = getRight().evaluate(context);
        if (operation == '+') {
            result = a + b;
        }
        if (operation == '-') {
            result = a - b;
        }
        if (operation == '*') {
            result = a * b;
        }
        if (operation == '/') {
            result = a / b;
        }
        return result;
    }

    public Operand getLeft() {
        return left;
    }

    public void setLeft(Operand left) {
        this.left = left;
    }

    public Operand getRight() {
        return right;
    }

    public void setRight(Operand right) {
        this.right = right;
    }
}
