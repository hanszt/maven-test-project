package hzt.creational_patterns.singleton_pattern;

class EagerInstantiation {

    private static final EagerInstantiation SINGLETON = new EagerInstantiation();//Early, instance will be created at load time

    private EagerInstantiation() {
    }

    public static EagerInstantiation getSingleton() {
        return SINGLETON;
    }

    public void doSomething() {
        //write your code
    }
}
