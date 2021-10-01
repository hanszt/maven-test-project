package hzt.stream.predicates;

import hzt.stream.TestSampleGenerator;
import hzt.stream.model.Painting;
import org.junit.jupiter.api.Test;

import java.util.List;

import static hzt.stream.StreamUtils.by;
import static hzt.stream.predicates.StringPredicates.*;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StringPredicatesTest {

    @Test
    void testContains() {
        final String O = "o";
        final String A = "a";
        final List<Painting> paintingList = TestSampleGenerator.getPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> painting.getName().contains(O) || painting.getName().contains(A))
                .toList();

        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::getName, containsAnyOf(O, A)))
                .toList();

        result.forEach(System.out::println);
        assertEquals(expected, result);
    }

    @Test
    void testIsEqualIgnoreCase() {
        final String NAME1 = "Meisje Met dE paRel";
        final String NAME2 = "GUERNICA";
        final List<Painting> paintingList = TestSampleGenerator.getPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> painting.getName().equalsIgnoreCase(NAME1) || painting.getName().equalsIgnoreCase(NAME2))
                .toList();

        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::getName, isEqualIgnoreCase(NAME1).or(isEqualIgnoreCase(NAME2))))
                .toList();

        result.forEach(System.out::println);
        assertEquals(expected, result);
    }

    @Test
    void testStartsWith() {
        final String LE = "Le";
        final String ME = "Me";
        final List<Painting> paintingList = TestSampleGenerator.getPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> painting.getName().startsWith(LE) || painting.getName().startsWith(ME))
                .toList();

        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::getName, startsWith(LE).or(startsWith(ME))))
                .toList();

        result.forEach(System.out::println);
        assertEquals(expected, result);
    }

    @Test
    void testEndsWith() {
        final String EL = "el";
        final String HOED = "hoed";
        final String NON = "non";
        final List<Painting> paintingList = TestSampleGenerator.getPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> painting.getName().endsWith(EL) || painting.getName().endsWith(HOED) || painting.getName().endsWith(NON))
                .toList();

        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::getName, endsWithAnyOf(EL, HOED, NON)))
                .toList();

        result.forEach(System.out::println);
        assertEquals(expected, result);
    }

    @Test
    void testHasEqualLength() {
        final String NAME = "Meisje met de parel";
        final List<Painting> paintingList = TestSampleGenerator.getPaintingList();
        final List<Painting> expected = paintingList.stream()
                .filter(painting -> painting.getName().length() == NAME.length())
                .collect(toList());
        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::getName, hasEqualLength(NAME)))
                .collect(toList());
        result.forEach(System.out::println);
        assertEquals(expected, result);
    }

    @Test
    void testStringContainsAll() {
        final List<Painting> paintingList = TestSampleGenerator.getPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> {
                    boolean containsMeisje = painting.getName().contains("Meisje");
                    boolean containsDe = painting.getName().contains("de");
                    boolean containsA = painting.getName().contains("a");
                    return containsMeisje && containsDe && containsA;
                }).toList();

        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::getName, containsAllOf("Meisje", "de", "a")))
                .toList();

        result.forEach(System.out::println);
        assertEquals(expected, result);
    }

    @Test
    void testContainsAnyOf() {
        //arrange
        final String EL = "el";
        final String HOED = "hoed";
        final String NON = "non";
        final List<Painting> paintingList = TestSampleGenerator.getPaintingList();
        final List<Painting> expected = paintingList.stream()
                .filter(painting -> painting.getName().contains(EL) || painting.getName().contains(HOED) || painting.getName().contains(NON))
                .toList();
        //act
        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::getName, containsAnyOf(EL, HOED, NON)))
                .toList();

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
        final List<Painting> paintingList = TestSampleGenerator.getPaintingList();

        final List<Painting> expected = paintingList.stream()
                .filter(painting -> !(painting.getName().contains(EL) || painting.getName().contains(HOED) || painting.getName().contains(NON)))
                .toList();
        //act
        final List<Painting> result = paintingList.stream()
                .filter(by(Painting::getName, containsNoneOf(EL, HOED, NON)))
                .toList();

        System.out.println("Input:");
        paintingList.forEach(System.out::println);
        System.out.println("result:");
        result.forEach(System.out::println);
        //assert
        assertEquals(expected, result);
    }
}
