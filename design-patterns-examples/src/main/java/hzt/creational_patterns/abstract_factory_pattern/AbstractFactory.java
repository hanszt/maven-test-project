package hzt.creational_patterns.abstract_factory_pattern;

abstract class AbstractFactory {

    public abstract Bank getBank(String bank);

    public abstract Loan getLoan(String loan);
}
