package hzt.only_jdk.sealed_interface;

public sealed interface Quackable permits Duck {

    <T> void addQuacks(T[] args);
}
