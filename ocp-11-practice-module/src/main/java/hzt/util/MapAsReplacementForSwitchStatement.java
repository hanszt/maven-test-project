package hzt.util;

import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;

public class MapAsReplacementForSwitchStatement {

    enum Test {

        TEST("Hans");

        private final String name;

        Test(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private final Map<String, Function<String, ?>> keyToFunctionMap = Map.of(
            "1", this::printSomething,
            "2", this::parseInteger,
            "3", e -> parseDouble());

    private Double parseDouble() {
        return Double.parseDouble("4.");
    }

    private Integer parseInteger(String string) {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException e) {
            System.out.println(string + " can not be parsed to an integer...");
            string.lines();
            return 0;
        }
    }

    private String printSomething(String string) {
        System.out.println(string);
        return string;
    }

    public static void main(String... args) {
        new MapAsReplacementForSwitchStatement().start();
    }

    private void start() {
        var scanner = new Scanner(System.in);
        System.out.println("choose between " + String.join(", ", keyToFunctionMap.keySet()));
        String input = scanner.next();
        System.out.println("What action do you want to apply?");
        String action = scanner.next();
        Optional.ofNullable(keyToFunctionMap.get(input))
                .ifPresentOrElse(
                        function -> function.apply(action),
                        () -> System.out.println("Choose another value"));
    }
}
