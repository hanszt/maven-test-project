package hzt.stream.predicates;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectionPredicatesTest {

    @Test
    @Disabled("Needs to be implemented")
    void testContainsAll() {
        assertNotNull(CollectionPredicates.containsAll());
    }

    @Test
    void testContainsAny() {
        assertNotNull(CollectionPredicates.containsAny());
    }

    @Test
    void testContainsNone() {
        assertNotNull(CollectionPredicates.containsNone());
    }
}
