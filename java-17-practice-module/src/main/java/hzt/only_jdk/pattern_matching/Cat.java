package hzt.only_jdk.pattern_matching;

import java.time.LocalDate;

public class Cat extends Animal {

    public Cat(String name, LocalDate dateOdBirth) {
        super(name, dateOdBirth);
    }

    public String getMiauw() {
        return "Miauw!";
    }
}
