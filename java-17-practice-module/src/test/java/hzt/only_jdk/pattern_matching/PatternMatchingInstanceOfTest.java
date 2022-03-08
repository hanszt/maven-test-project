package hzt.only_jdk.pattern_matching;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PatternMatchingInstanceOfTest {

    @Test
    void testAnimal() {
        Animal fluffy = new Dog("Fluffy", LocalDate.of(2015, 5, 23));
        Animal mickel = new Cat("Mickel", LocalDate.ofYearDay(2005, 243));
        assertEquals("Woof!", getAnimalSound(fluffy));
        assertEquals("Miauw!", getAnimalSound(mickel));
    }

    private static String getAnimalSound(Animal animal) {
        if (animal instanceof Dog dog) return dog.getBarc();
        else if (animal instanceof Cat cat)return cat.getMiauw();
        throw new IllegalStateException("Unknown animal");
    }
}
