package hzt.behavioural_patterns.interpreter_pattern;

import java.util.Map;

interface Operand {
    double evaluate(Map<String, Integer> context);

    void traverse(int level);
}
