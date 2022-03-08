package hzt;

import org.junit.jupiter.api.Test;

import java.util.ServiceLoader;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

// Test 1 Q 13, Q 20, Q 30
class ServiceLoaderSampleTest {

    @Test
    void testServiceLoaderImplementsIterable() {
        Set<Class<?>> interfaces = Set.of(ServiceLoader.class.getInterfaces());

        assertEquals(1, interfaces.size());
        assertTrue(interfaces.contains(Iterable.class));
    }

    @Test
    void testServiceLoaderFromStringRequiresUsesClauseInModuleInfo() {
        ServiceLoader<String> serviceLoader = ServiceLoader.load(String.class);
        System.out.println(serviceLoader);

        final var count = serviceLoader.stream().count();

        assertEquals(0, count);
    }

}
