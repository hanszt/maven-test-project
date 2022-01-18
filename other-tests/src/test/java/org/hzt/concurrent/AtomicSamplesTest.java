package org.hzt.concurrent;

import org.hzt.model.Person;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AtomicSamplesTest {

    @Test
    void testAtomicReference() {
        final AtomicReference<Person> personAtomicReference = new AtomicReference<>();
        final var pepijn = new Person("Pepijn");
        assertNull(personAtomicReference.getAndSet(pepijn));
        final var judith = new Person("Judith");
        assertEquals(pepijn, personAtomicReference.getAndSet(judith));
        assertTrue(personAtomicReference.compareAndSet(judith, pepijn));
    }
}
