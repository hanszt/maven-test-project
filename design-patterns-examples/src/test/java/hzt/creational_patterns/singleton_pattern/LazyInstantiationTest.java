package hzt.creational_patterns.singleton_pattern;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LazyInstantiationTest {

    @Test
    void testUnSafeLazyInitialization() {
        final var unsafeLazySingleton = LazyInstantiation.getUnsafeLazyInstance();
        final var unsafeHello = unsafeLazySingleton.hello();

        assertAll(
                () -> assertEquals("Unsafe hello", unsafeHello),
                () -> assertSame(unsafeLazySingleton, LazyInstantiation.getUnsafeLazyInstance())
        );
    }

    @Test
    void testSafeLazyInitialization() {
        final var isInitialized = LazyInstantiation.isSafelyInitialized();
        final var safeLazySingleton = LazyInstantiation.getSafeLazyInstance();
        final var safeHello = safeLazySingleton.hello();

        assertAll(
                () -> assertFalse(isInitialized),
                () -> assertTrue(LazyInstantiation.isSafelyInitialized()),
                () -> assertEquals("Safe hello", safeHello),
                () -> assertSame(safeLazySingleton, LazyInstantiation.getSafeLazyInstance())
        );
    }
}
