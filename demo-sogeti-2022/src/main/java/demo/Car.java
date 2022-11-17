package demo;

import static demo.It.println;

public record Car(String brand, Type type, int price) {

    public enum Type {STATION_CAR, HATCH_BACK, COUPE, SUV, SEDAN}

    public boolean isPriceLessThanEqual20_000() {
        println("Checking price " + price);
        return price <= 20_000;
    }

    public boolean isOfTypeStationCar() {
        println("Checking type " + type);
        return type == Type.STATION_CAR;
    }

    public String printAndGetBrand() {
        println("Getting brand " + brand);
        return brand;
    }
}
