package hzt.interfaces;

@FunctionalInterface
public interface FunctionalInterfaceContainingMoreThanOneAbstractMethod {

    int CONSTANT_VAL = 4;

    int apply(int i);

    String toString();

    boolean equals(Object o);

    int hashCode();

    default void printHello() {
        System.out.println(getText());
    }

    private static String getText() {
        return "Hello";
    }

    static void printFromStaticMethod() {
        System.out.println("Printed from static method");
    }

}
