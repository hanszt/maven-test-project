package hzt.only_jdk.sealed_class;

public sealed class Fruit permits Apple, Banana, CitrusFruit {

    private final String origin;
    private double weight;

    public Fruit(String origin) {
        this.origin = origin;
    }

    public String getOrigin() {
        return origin;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Fruit{" +
                "origin='" + origin + '\'' +
                ", weight=" + weight +
                '}';
    }
}
