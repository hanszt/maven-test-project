package hzt.only_jdk;

import org.hzt.utils.collections.ListX;
import org.hzt.utils.iterables.IterableX;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class OnlyJava17CompatibleTest {

    @Test
    void testJava11AndBeforeCompilerTripperWorkFineInJava17() {
        assertDoesNotThrow(() -> empty());
    }

    // causes compiler error on java 11 and older:
    // java: Compilation failed: internal java compiler error
    public static <T> IterableX<T> empty() {
        return ListX.of(() -> new Iterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                throw new NoSuchElementException();
            }
        });
    }
}
