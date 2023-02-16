package hzt.preview.pattern_matching;

import java.time.LocalDate;

public final class Cat extends Animal {

    public Cat(String name, LocalDate dateOdBirth) {
        super(name, dateOdBirth);
    }

    public String getMiauw() {
        return "Miauw!";
    }
}
