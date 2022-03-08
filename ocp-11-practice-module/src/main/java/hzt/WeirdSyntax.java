package hzt;

public class WeirdSyntax {

    public static void main(String[] args) {
        boolean b1 = false;
        int i1 = 2;
        int i2 = 3;
        if (b1 = i1 == i2) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
