package hzt.creational_patterns.singleton_pattern;

public class LazyInstantiation {

    private static LazyInstantiation singleton;

    private LazyInstantiation() {
    }

    public static LazyInstantiation getSingleton() {
        if (singleton == null) {
            synchronized (LazyInstantiation.class) {
                if (singleton == null) {
                    //instance will be created at request time
                    singleton = new LazyInstantiation();
                }
            }
        }
        return singleton;
    }

    public void doSomething() {
        //write your code
    }
}

