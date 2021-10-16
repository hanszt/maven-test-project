package hzt.stream.predicates;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CollectionPredicatesTest {

    @Test
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
