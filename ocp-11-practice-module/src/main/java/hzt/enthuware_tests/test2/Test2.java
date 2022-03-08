package hzt.enthuware_tests.test2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class Test2 {

    public static void main(String[] args) {
        final @NonNull String s = "Hallo   ";
        final var test2 = new Test2();
        final var trimmed = test2.testVarInFunctionNotAllowed(s);
        assert "Hallo".equals(trimmed);

        test2.chainedAssignment();
    }

    public String testVarInFunctionNotAllowed(@NonNull String s) {
        Function<String, String> stringTrimFunction = string -> string.trim();
        return stringTrimFunction.apply(s);
    }

    public void chainedAssignment() {
        //allowed
//        int a, b, c; a = b = c = 100;
//        int a=100, b, c;
        int a, b, c;
        a = b = c = 100;

//      not allowed
//        int a = b = c = 100;
//        var a = 100, b = 10;
//        var a = b;

        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("c = " + c);
    }

}

abstract class AmazingClass {
    void amazingMethod(Collection c) {
        System.out.println("Got collection");
    }

}

// test 2 q 1: 'void amazingMethod(Collection l)' not overridden by 'void amazingMethod(List l)'
class SpecialAmazingClass extends AmazingClass {
    void amazingMethod(List l) {
        System.out.println("Got list");
    }

    public static void main(String[] args) {
        List<String> al = new ArrayList<String>();
        Collection<String> c = al;
        AmazingClass ac = new SpecialAmazingClass();
        ac.amazingMethod(c);
    }
}

//test 2 q 21
//run with a b c as args
class FunWithArgs {
    public static void main(String[][] args) {
        System.out.println(args[0][1]);
    }

    public static void main(String[] args) {
        var fwa = new FunWithArgs();
        String[][] newargs = {args};
        fwa.main(newargs);
    }
}

class Instinker {

    //test 2 q 32
    //The args array is never null! Notice the simulated else. There is no else!
    public static void main(String[] args) {
        var hasParams = (args == null ? false : true);
        if (hasParams) {
            System.out.println("has params");
        } {
            System.out.println("no params");
        }
    }
}
