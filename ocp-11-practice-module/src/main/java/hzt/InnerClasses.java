package hzt;

class Outsider {

    class Insider {

        void print() {
            System.out.println("Inner called");
        }
    }

    static class StaticInner {

        void print() {
            System.out.println("Static inner called");
        }
    }
}

public class InnerClasses {

    public static void main(String... args) {
        Outsider os = new Outsider();
        Outsider.Insider insider = os.new Insider();
        insider.print();
        Outsider.StaticInner staticInner = new Outsider.StaticInner();
        staticInner.print();
    }
}
