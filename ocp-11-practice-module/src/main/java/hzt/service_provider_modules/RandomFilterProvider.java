package hzt.service_provider_modules;

import hzt.interfaces.MyFilter;

import java.util.Random;

public final class RandomFilterProvider {

    private static final Random RANDOM = new Random();

    private RandomFilterProvider() {
    }

    public static MyFilter provider() {
        return () -> RANDOM.nextDouble() < .5;
    }
}
