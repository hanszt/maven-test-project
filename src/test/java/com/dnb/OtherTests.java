package com.dnb;

import com.dnb.model.Bic;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class OtherTests {

    @Test
    void testChars() {
        IntStream.range(33, 900).mapToObj(this::toCharacter).forEach(System.out::println);
        System.out.println();
        "Hello".chars().mapToObj(this::toCharacter).forEach(System.out::println);
        String.format("Hello,%nI'm Hans").lines().forEach(System.out::println);
        char c = 'ͽ';
        assertEquals(893, c);
    }

    private Character toCharacter(int i) {
        return (char) i;
    }

    @Test
    void testStreamFindFirstNotNullSafe() {
        List<Bic> bics = List.of(new Bic(null), new Bic(null));
        assertThrows(NullPointerException.class, () -> getAnyNameThrowingNull(bics));
    }

    @Test
    void testStreamFindFirstDoesNotThrowNull() {
        List<Bic> bics = List.of(new Bic(null), new Bic(null));
        assertTrue(getAnyName(bics).isEmpty());
    }

    @Test
    void testStreamFindFirstNotNullSafeButNotThrowing() {
        String expected = "Sophie";
        Optional<String> anyName = getAnyNameThrowingNull(List.of(new Bic(expected), new Bic(null)));
        anyName.ifPresentOrElse(name -> assertEquals(expected, name), () -> fail("Not present"));
    }

    private Optional<String> getAnyNameThrowingNull(List<Bic> bics) {
        return bics.stream()
                .filter(Objects::nonNull)
                .map(Bic::getName)
                .findAny();
    }

    private Optional<String> getAnyName(List<Bic> bics) {
        return bics.stream()
                .map(Bic::getName)
                .filter(Objects::nonNull)
                .findAny();
    }

    @Test
    void throwsNullPointer() {
        Set<String> stringSet = new HashSet<>();
        stringSet.remove(null);
    }
}
