package hzt.creational_patterns.singleton_pattern;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LazyInstantiationTest {

    @Test
    void testSafeLazyInitialization() {
        final var unsafeLazySingleton = LazyInstantiation.getUnsafeLazyInstance();
        final var unsafeHello = unsafeLazySingleton.hello();

        final var safelyInitialized = LazyInstantiation.isSafelyInitialized();

        final var safeLazySingleton = LazyInstantiation.getSafeLazyInstance();
        final var safeHello = safeLazySingleton.hello();

        assertAll(
                () -> assertFalse(safelyInitialized),
                () -> assertEquals("Unsafe hello", unsafeHello),
                () -> assertTrue(LazyInstantiation.isSafelyInitialized()),
                () -> assertEquals("Safe hello", safeHello)
        );
    }
}
