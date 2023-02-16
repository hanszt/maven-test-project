package hzt.preview.pattern_matching;

import java.time.LocalDate;

public final class Dog extends Animal {
    
    public Dog(String name, LocalDate dateOfBirth) {
        super(name, dateOfBirth);
    }
    
    public String getBarc() {
        return "Woof!";
    }
}
