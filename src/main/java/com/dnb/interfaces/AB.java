package com.dnb.interfaces;

import com.dnb.model.Bic;

import java.util.ArrayList;
import java.util.List;

public class AB implements A {

    public static void main(String[] args) {
        Bic bic = null;
        System.out.println(bic);
        List<String> strings = new ArrayList<>(List.of("Hallo" ,"hoe", "gaat", "het?"));
        strings.removeIf(string -> string.contains("h"));
        System.out.println("strings = " + strings);
        new AB().test();
    }

    private void test() {
        System.out.println(size());
    }
}
