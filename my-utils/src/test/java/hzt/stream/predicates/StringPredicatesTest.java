package hzt.stream.predicates;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Painting;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static hzt.stream.StreamUtils.by;
import static hzt.stream.predicates.StringPredicates.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StringPredicatesTest {

    @Test
    void testContains() {
        final String O = "o";
        final String A = "first";
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> painting.name().contains(O) || painting.name().contains(A))
                .collect(Collectors.toUnmodifiableList());

        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::name, containsAnyOf(O, A)))
                .collect(Collectors.toUnmodifiableList());

        result.forEach(System.out::println);
        assertEquals(expected, result);
    }

    @Test
    void testIsEqualIgnoreCase() {
        final String NAME1 = "Meisje Met dE paRel";
        final String NAME2 = "GUERNICA";
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> painting.name().equalsIgnoreCase(NAME1) || painting.name().equalsIgnoreCase(NAME2))
                .collect(Collectors.toUnmodifiableList());

        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::name, isEqualIgnoreCase(NAME1).or(isEqualIgnoreCase(NAME2))))
                .collect(Collectors.toUnmodifiableList());

        result.forEach(System.out::println);
        assertEquals(expected, result);
    }

    @Test
    void testStartsWith() {
        final String LE = "Le";
        final String ME = "Me";
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> painting.name().startsWith(LE) || painting.name().startsWith(ME))
                .collect(Collectors.toUnmodifiableList());

        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::name, startsWith(LE).or(startsWith(ME))))
                .collect(Collectors.toUnmodifiableList());

        result.forEach(System.out::println);
        assertEquals(expected, result);
    }

    @Test
    void testEndsWith() {
        final String EL = "el";
        final String HOED = "hoed";
        final String NON = "non";
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> painting.name().endsWith(EL) || painting.name().endsWith(HOED) || painting.name().endsWith(NON))
                .collect(Collectors.toUnmodifiableList());

        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::name, endsWithAnyOf(EL, HOED, NON)))
                .collect(Collectors.toUnmodifiableList());

        result.forEach(System.out::println);
        assertEquals(expected, result);
    }

    @Test
    void testHasEqualLength() {
        final String NAME = "Meisje met de parel";
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> painting.name().length() == NAME.length())
                .collect(Collectors.toUnmodifiableList());

        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::name, hasEqualLength(NAME)))
                .collect(Collectors.toUnmodifiableList());

        result.forEach(System.out::println);

        assertEquals(expected, result);
    }

    @Test
    void testStringContainsAll() {
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> {
                    boolean containsMeisje = painting.name().contains("Meisje");
                    boolean containsDe = painting.name().contains("de");
                    boolean containsA = painting.name().contains("first");
                    return containsMeisje && containsDe && containsA;
                }).collect(Collectors.toUnmodifiableList());

        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::name, containsAllOf("Meisje", "de", "first")))
                .collect(Collectors.toUnmodifiableList());

        result.forEach(System.out::println);
        assertEquals(expected, result);
    }

    @Test
    void testContainsAnyOf() {
        //arrange
        final String EL = "el";
        final String HOED = "hoed";
        final String NON = "non";
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> painting.name().contains(EL) || painting.name().contains(HOED) || painting.name().contains(NON))
                .collect(Collectors.toUnmodifiableList());
        //act
        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::name, containsAnyOf(EL, HOED, NON)))
                .collect(Collectors.toUnmodifiableList());

        System.out.println("Input:");
        paintingList.forEach(System.out::println);
        System.out.println("result:");
        result.forEach(System.out::println);
        //assert
        assertEquals(expected, result);
    }

    @Test
    void testContainsNone() {
        //arrange
        final String EL = "el";
        final String HOED = "hoed";
        final String NON = "non";
        final List<Painting> paintingList = TestSampleGenerator.createPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> !(painting.name().contains(EL) || painting.name().contains(HOED) || painting.name().contains(NON)))
                .collect(Collectors.toUnmodifiableList());
        //act
        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::name, containsNoneOf(EL, HOED, NON)))
                .collect(Collectors.toUnmodifiableList());

        System.out.println("Input:");
        paintingList.forEach(System.out::println);
        System.out.println("result:");
        result.forEach(System.out::println);
        //assert
        assertEquals(expected, result);
    }
}
