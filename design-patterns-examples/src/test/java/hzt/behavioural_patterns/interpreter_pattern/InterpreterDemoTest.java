package hzt.behavioural_patterns.interpreter_pattern;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static hzt.behavioural_patterns.interpreter_pattern.InterpreterDemo.buildSyntaxTree;
import static hzt.behavioural_patterns.interpreter_pattern.InterpreterDemo.convertToPostfix;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InterpreterDemoTest {

    @Test
    void testInterpreterPattern() {
        final var conversionFromCelsiusToFahrenheit = "celsius * 9 / 5 + thirty";

        String postfix = convertToPostfix(conversionFromCelsiusToFahrenheit);
        Operand operand = buildSyntaxTree(postfix);
        operand.traverse(1);

        Map<String, Integer> context = Map.of("thirty", 30, "celsius", 23);

        final var temperatureInFahrenheit = operand.evaluate(context);
        assertEquals(71.4, temperatureInFahrenheit);
    }

}
