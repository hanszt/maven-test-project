package com.dnb;

import com.dnb.model.Bic;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.dnb.ReflectionSample.PicassoFunctions.integerPropertyAccessor;
import static java.util.stream.Collectors.toUnmodifiableMap;
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
    void testWeirdBehaviourIntegerPropertyAccessor() {
        final var bic1 = new Bic("1", "1");
        List<Bic> bics = List.of(bic1, new Bic("2", "2"), new Bic("3", "hallo"));
        //The map contains integer type keys, but its hash is created for the string type? Weird behaviour
        Map<Integer, Bic> integerToBicMap = bics.stream()
                .collect(toUnmodifiableMap(integerPropertyAccessor("name"), bic -> bic));
        //weird outcome, map with keys of type integer gives first value back when lookup key is of type String
        // but not when it is of type integer!
        //So lets not use the integerPropertyAccessor function
        assertEquals(integerToBicMap.get("1"), bic1);
        assertNotEquals(integerToBicMap.get(1), bic1);
        assertTrue(integerToBicMap.containsKey("hallo"));
        assertFalse(integerToBicMap.containsKey(1));
    }

    @Test
    void testThrowsExceptionWhenWrongPropertyName() {
        final var bic1 = new Bic("1", "");
        final var integerFunction = integerPropertyAccessor("bic.id");
        assertThrows(IllegalStateException.class, () -> integerFunction.apply(bic1));
    }

    @Test
    void testGetPackageVersionInfo() {
        // null means not known
        assertNull(getClass().getPackage().getImplementationVersion());
    }
}
