package hzt.creational_patterns.singleton_pattern;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SerializationInSingletonPatternTest {

    @Test
    void testReadResolveEnsuresSingleInstantiation() {
        final var serializationInSingletonPattern = new SerializationInSingletonPattern();
        final var lazyInstantiation = (LazyInstantiation) assertDoesNotThrow(serializationInSingletonPattern::readResolve);
        final var hello = lazyInstantiation.hello();
        assertEquals("Unsafe hello", hello);
    }

}
