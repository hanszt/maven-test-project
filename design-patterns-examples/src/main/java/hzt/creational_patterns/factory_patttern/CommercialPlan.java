package hzt.creational_patterns.factory_patttern;

public class CommercialPlan extends Plan{

    @Override
    void getRate() {
        super.rate = 7.5;
    }
}
