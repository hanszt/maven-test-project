package com.dnb;

import com.dnb.model.Bic;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.dnb.ReflectionSample.PicassoFunctions.integerPropertyAccessor;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * A class to test reflection examples.
 * @see ReflectionSample
 * @author Hans Zuidervaart
 */
class ReflectionSampleTest {

    @Test
    void testSetFinalFieldNameToNewValue() throws NoSuchFieldException, IllegalAccessException {
        var bic = new Bic("BicName");
        ReflectionSample.setFinalFieldNameToNewValue(bic, "Hans");
        assertEquals("Hans", bic.getName());
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Test
    @Disabled("This is a non important test")
    void testWeirdBehaviourIntegerPropertyAccessor() {
        final var bic1 = new Bic("1", "1");
        List<Bic> bics = List.of(bic1, new Bic("2", "2"), new Bic("3", "hallo"));
        //The map contains integer type keys but it's hash is created for the string type? Weird behaviour
        Map<Integer, Bic> integerToBicMap = bics.stream()
                .collect(toUnmodifiableMap(integerPropertyAccessor("name"), bic -> bic));
        //weird outcome, map with keys of type integer gives a value back when lookup key is of type String
        // but not when it it is of type integer!
        //So lets not use the integerPropertyAccessor function
        assertEquals(integerToBicMap.get("1"), bic1);
        assertNotEquals(integerToBicMap.get(1), bic1);
        assertTrue(integerToBicMap.containsKey("hallo"));
        assertFalse(integerToBicMap.containsKey(1));
    }

    @Test
    @Disabled("This test is just for a curiosity not important")
    void testThrowsExceptionWhenWrongPropertyName() {
        final var bic1 = new Bic("1", "");
        assertThrows(IllegalStateException.class, () -> integerPropertyAccessor("bic.id").apply(bic1));

    }
}