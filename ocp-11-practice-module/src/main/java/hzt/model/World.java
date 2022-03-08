package hzt.model;

public class World {

    public static final World EARTH = new World() {

        public static final int WEIGHT = 23_423_424;

        public void printName() {
            System.out.println("Aarde");
        }
    };

}
