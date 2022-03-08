package hzt.behavioural_patterns.interpreter_pattern;

import java.util.*;

public class BeforeRefactoring {

    public static boolean precedence(char a, char b) {
        String high = "*/";
        String low = "+-";
        if (a == '(') return false;
        if (a == ')' && b == '(') return false;
        if (b == '(') return false;
        if (b == ')') return true;
        if (high.indexOf(a) > -1 && low.indexOf(b) > -1) return true;
        if (high.indexOf(a) > -1 && high.indexOf(b) > -1) return true;
        return (low.indexOf(a) > -1 && low.indexOf(b) > -1);
    }

    public static String convertToPostfix(String expr) {
        Deque<Character> operationsStack = new ArrayDeque<>();
        StringBuilder out = new StringBuilder();
        String operations = "+-*/()";
        char topSymbol = '+';
        boolean empty;
        String[] tokens = expr.split(" ");
        for (String token : tokens) {
            if (operations.indexOf(token.charAt(0)) == -1) {
                out.append(token);
                out.append(' ');
            } else {
                while (!(empty = operationsStack.isEmpty()) &&
                        precedence(topSymbol = operationsStack.pop(), token.charAt(0))) {
                    out.append(topSymbol);
                    out.append(' ');
                }
                if (!empty) {
                    operationsStack.push(topSymbol);
                }
                if (empty || token.charAt(0) != ')') {
                    operationsStack.push(token.charAt(0));
                } else {
                    topSymbol = operationsStack.pop();
                }
            }
        }
        while (!operationsStack.isEmpty()) {
            out.append(operationsStack.pop());
            out.append(' ');
        }
        return out.toString();
    }

    public static double processPostfix(String postfix, Map<String, Integer> map) {
        Deque<Double> stack = new ArrayDeque<>();
        String operations = "+-*/";
        String[] tokens = postfix.split(" ");
        for (String token : tokens) {
            // If token is a number or variable
            if (operations.indexOf(token.charAt(0)) == -1) {
                double term;
                try {
                    term = Double.parseDouble(token);
                } catch (NumberFormatException ex) {
                    term = map.get(token);
                }
                stack.push(term);

                // If token is an operator
            } else {
                double b = stack.pop();
                double a = stack.pop();
                if (token.charAt(0) == '+') {
                    a = a + b;
                }
                if (token.charAt(0) == '-') {
                    a = a - b;
                }
                if (token.charAt(0) == '*') {
                    a = a * b;
                }
                if (token.charAt(0) == '/') {
                    a = a / b;
                }
                stack.push(a);
            }
        }
        return stack.pop();
    }

    public static void main(String[] args) {
        String infix = "C * 9 / 5 + 32";
        String postfix = convertToPostfix(infix);
        System.out.println("Infix:   " + infix);
        System.out.println("Postfix: " + postfix);
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i <= 100; i += 10) {
            map.put("C", i);
            System.out.println("C is " + i + ",  F is " + processPostfix(postfix, map));
        }
    }
}
