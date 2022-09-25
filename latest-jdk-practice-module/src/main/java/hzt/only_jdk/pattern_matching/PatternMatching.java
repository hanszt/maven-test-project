package hzt.only_jdk.pattern_matching;

import java.time.LocalDate;
import java.util.Collection;

public class PatternMatching {

    public static void main(String[] args) {
        Animal animal = Math.random() < .5 ? new Dog("dog1", LocalDate.EPOCH) : new Cat("cat1", LocalDate.EPOCH);
        final var animalSound = getAnimalSound(animal);
        System.out.println("animalSound = " + animalSound);
    }

    private static String getAnimalSound(Animal animal) {
        return switch (animal) {
            case Dog dog -> dog.getBarc();
            case Cat cat -> cat.getMiauw();
            default -> throw new IllegalStateException("Unexpected value: " + animal);
        };
    }

    /**
     * @param o the object to pattern match
     * @return an integer based on the objects type
     *
     * @see <a href="https://youtu.be/6pN0Ymsl1H0?t=2158">Pattern matching and record classes</a>
     */
    @SuppressWarnings("ConstantConditions")
    static int toInteger(Object o) {
        return switch (o) {
            case null -> 0;
            case String s when s.length() < 4 -> s.length() + 4;
            case String s -> s.length();
            case Collection<?> c -> c.size();
            case Point(int x, int y) when x + y < 10 -> x * y;
            case Point(int x, int y) -> x + y;
//            this could also be done. Not yet recognized by Intellij (2022-009-25)
//            case Point(int x, var y) -> x + y;
            case Rectangle(Point(int x1, int y1), Point(int x2, int y2)) -> x1 + x2 + y1 + y2;
            default -> throw new IllegalStateException("Could not obtain length...");
        };
    }

    record Point(int x, int y) {
    }

    record Rectangle(Point upperLeft, Point lowerRight) {
    }

}
