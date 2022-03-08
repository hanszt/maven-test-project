package hzt.only_jdk.sealed_class;

public sealed class CitrusFruit extends Fruit permits Lemon, Orange {
    public CitrusFruit(String origin) {
        super(origin);
    }
}
