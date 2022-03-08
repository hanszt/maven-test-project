package hzt.interfaces;

import hzt.service_provider_modules.Adder;

public class AdderExtension extends Adder {

    private int value;
    private int value2;

    //    Source: Enthuware Java11OCP Test 1 Q 8
    //    Which of the following constructors MUST exist in SuperClass for SubClass to compile correctly?
    public AdderExtension(int value, int value2) {
        this.value = value;
        this.value2 = value2;
    }

    public AdderExtension(int value) {
        super(value);
    }
}
