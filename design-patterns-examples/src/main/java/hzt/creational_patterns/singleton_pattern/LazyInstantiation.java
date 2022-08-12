package hzt.creational_patterns.singleton_pattern;

public final class LazyInstantiation {

    private static LazyInstantiation singleton;

    private static boolean safelyInitialized = false;
    private final String hello;

    private LazyInstantiation(String hello) {
        this.hello = hello;
    }

    public String hello() {
        return hello;
    }

    /**
     * @return the LazyInstantiated class. This way is not recommended
     * @see <a href="https://rules.sonarsource.com/java/RSPEC-2168">Double-checked locking should not be used</a>
     */
    @SuppressWarnings({"squid:S2168", "DoubleCheckedLocking"})
    public static LazyInstantiation getUnsafeLazyInstance() {
        if (singleton == null) {
            synchronized (LazyInstantiation.class) {
                if (singleton == null) {
                    //instance will be created at request time
                    singleton = new LazyInstantiation("Unsafe hello");
                }
            }
        }
        return singleton;
    }

    public static LazyInstantiation getSafeLazyInstance() {
        return InstanceHolder.instance;
    }

    private static class InstanceHolder {

        // This will be lazily initialized
        private static final LazyInstantiation instance;

        static {
            instance = new LazyInstantiation("Safe hello");
            safelyInitialized = true;
        }
    }

    public static boolean isSafelyInitialized() {
        return safelyInitialized;
    }
}

