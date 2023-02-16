package hzt.behavioural_patterns.interpreter_pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class InterpreterDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterpreterDemo.class);

    @SuppressWarnings("squid:S3776")
    public static String convertToPostfix(String expr) {
        Deque<Character> operationsStack = new ArrayDeque<>();
        StringBuilder sb = new StringBuilder();
        String operations = "+-*/()";
        char topSymbol = '+';
        String[] tokens = expr.split(" ");
        for (String token : tokens) {
            if (operations.indexOf(token.charAt(0)) == -1) {
                sb.append(token).append(' ');
            } else {
                while (!operationsStack.isEmpty() && precedence(topSymbol = operationsStack.pop(), token.charAt(0))) {
                    sb.append(topSymbol).append(' ');
                }
                if (!operationsStack.isEmpty()) {
                    operationsStack.push(topSymbol);
                }
                if (operationsStack.isEmpty() || token.charAt(0) != ')') {
                    operationsStack.push(token.charAt(0));
                } else {
                    topSymbol = operationsStack.pop();
                }
            }
        }
        while (!operationsStack.isEmpty()) {
            sb.append(operationsStack.pop()).append(' ');
        }
        return sb.toString();
    }

    private static boolean precedence(char a, char b) {
        if (a == '(' || b == '(') {
            return false;
        }
        if (b == ')') {
            return true;
        }
        final String high = "*/";
        final String low = "+-";
        if ((high.indexOf(a) > -1 && low.indexOf(b) > -1) || (high.indexOf(a) > -1 && high.indexOf(b) > -1)) {
            return true;
        }
        return (low.indexOf(a) > -1 && low.indexOf(b) > -1);
    }

    public static Operand buildSyntaxTree(String tree) {
        Deque<Operand> stack = new ArrayDeque<>();
        String operations = "+-*/";
        String[] tokens = tree.split(" ");
        for (String token : tokens) {
            if (operations.indexOf(token.charAt(0)) == -1) {
                Operand term;
                try {
                    term = new Number(Double.parseDouble(token));
                } catch (NumberFormatException ex) {
                    term = new Variable(token);
                }
                stack.push(term);

                // If token is an operator
            } else {
                Expression expr = new Expression(token.charAt(0));
                expr.setRight(stack.pop());
                expr.setLeft(stack.pop());
                stack.push(expr);
            }
        }
        return stack.pop();
    }

    public static void main(String... args) {
        final var conversionFromCelsiusToFahrenheit = "celsius * 9 / 5 + thirty";

        String postfix = convertToPostfix(conversionFromCelsiusToFahrenheit);
        Operand operand = buildSyntaxTree(postfix);
        operand.traverse(1);

        Map<String, Integer> context = new HashMap<>(Map.of("thirty", 30));
        for (int celcius = 0; celcius <= 100; celcius += 10) {
            context.put("celsius", celcius);
            LOGGER.info("C is {},  F is {}", celcius, operand.evaluate(context));
        }
    }
}
