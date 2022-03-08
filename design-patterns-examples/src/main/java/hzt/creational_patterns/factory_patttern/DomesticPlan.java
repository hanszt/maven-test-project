package hzt.creational_patterns.factory_patttern;

public class DomesticPlan extends Plan{

    @Override
    void getRate() {
        super.rate = 3.5;
    }
}
