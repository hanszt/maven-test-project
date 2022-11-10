package demo;

public record Car(String brand, Type type, int price) {

    public enum Type {STATION_WAGON, HATCH_BACK, COUPE, SUV, SEDAN}
}
