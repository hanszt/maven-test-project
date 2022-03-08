package hzt.only_jdk.pattern_matching;

import java.time.LocalDate;

public class Dog extends Animal {
    
    public Dog(String name, LocalDate dateOdBirth) {
        super(name, dateOdBirth);
    }
    
    public String getBarc() {
        return "Woof!";
    }
}
