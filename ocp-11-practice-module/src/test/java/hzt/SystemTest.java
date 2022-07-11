package hzt;

import org.junit.jupiter.api.Test;

import java.io.Console;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

class SystemTest {

    @Test
    void testSystemGetProperties() {
        final Map<Object, Object> properties = System.getProperties();

        properties.forEach((key, value) -> System.out.println("key: " + key + ", value: " + value));
        final var desktop = properties.get("sun.desktop");
        if (desktop != null) {
            assertEquals("windows", desktop);
        }
        assertFalse(properties.isEmpty());
    }

    //q 17 test 6
    @Test
    void testSystemGetConsoleCanReturnNull() {
        Console c = System.console();
        assertNull(c);//1
        //noinspection ConstantConditions
        if (c != null) {
            String line = c.readLine("Please enter your name:"); //2
            System.out.println("Hello, " + line); //3
        }
    }
}
