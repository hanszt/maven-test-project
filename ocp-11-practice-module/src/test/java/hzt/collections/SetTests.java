package hzt.collections;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SetTests {

    @Test
    @Disabled("Takes to long")
    void testUnboundedSetAddThrowsOutOfMemoryError() {
        assertThrows(OutOfMemoryError.class, () -> fillSetUntilOutOfMemory());
    }

    private void fillSetUntilOutOfMemory() {
        Set<String> set = new HashSet<>();
        try {
            int counter = 0;
            while (true) {
                set.add("hoi" + counter);
                counter++;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            System.out.println(set.size());
        }
    }

}
