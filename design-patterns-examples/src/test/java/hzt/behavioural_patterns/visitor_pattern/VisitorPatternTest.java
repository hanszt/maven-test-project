package hzt.behavioural_patterns.visitor_pattern;

import org.hzt.test.TestSampleGenerator;
import org.hzt.test.model.Museum;
import org.hzt.test.model.Painter;
import org.hzt.test.model.Painting;
import org.hzt.test.model.Person;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VisitorPatternTest {

    @Test
    void testVisitRegisteredObjectsInVanGoghMuseum() {
        final var museumList = TestSampleGenerator.createMuseumList();

        final var vanGoghMuseum = anyMuseum(museumList, VisitorPatternTest::isVanGoghMuseum);

        final var list = Visitable.visiting(
                        museum -> museum,
                        museum -> museum.getMostPopularPainting().painter(),
                        Museum::getOldestPainting,
                        museum -> museum.getPaintings().get(1)
                )
                .makeVisitable(vanGoghMuseum)
                .stream(buildStringVisitor())
                .toList();

        list.forEach(System.out::println);

        final var museumName = "Visited museum: " + vanGoghMuseum.getName();
        final var painterName = "Visited painter: " + vanGoghMuseum.getMostPopularPainting().painter().getFirstName();
        final var oldestPaintingName = "Visited painting: " + vanGoghMuseum.getOldestPainting().name();
        final var secondPaintingName = "Visited painting: " + vanGoghMuseum.getPaintings().get(1).name();

        assertEquals(List.of(museumName, painterName, oldestPaintingName, secondPaintingName), list);
    }

    @Test
    void testVisitRegisteredObjectsInMuseumList() {
        final var museumList = TestSampleGenerator.createMuseumList();

        final var list = Visitable.visiting(
                        museum -> museum,
                        museum -> museum.getMostPopularPainting().painter(),
                        Museum::getOldestPainting,
                        museum -> museum.getPaintings().get(0)
                ).makeVisitable(museumList)
                .stream(buildStringVisitor())
                .toList();

        list.forEach(System.out::println);
        System.out.println();
        museumList.forEach(System.out::println);

        assertEquals(16, list.size());
    }

    @Test
    void testVisitPerson() {
        Person person = new Person("Piet");
        person.setMail("test@mail.com");

        final var list = Visitable.visiting(
                        (Person student) -> student,
                        Person::getMail
                ).makeVisitable(person)
                .stream(buildStringVisitor())
                .toList();

        assertEquals(List.of("Visited person: Piet", "test@mail.com"), list);
    }

    @Test
    void testThrowsWhenVisitingUnregisteredType() {
        final var museumList = TestSampleGenerator.createMuseumList();

        final var vanGoghMuseum = anyMuseum(museumList, VisitorPatternTest::isVanGoghMuseum);

        final var visitable = Visitable
                .visiting(Museum::getDateOfOpening, Museum::getMostPopularPainting)
                .makeVisitable(vanGoghMuseum)
                .stream(buildStringVisitor());

        //noinspection ResultOfMethodCallIgnored
        final var exception = assertThrows(IllegalArgumentException.class, visitable::toList);
        assertEquals("No action registered for type: LocalDate", exception.getMessage());
    }

    private static Visitor<String> buildStringVisitor() {
        return Visitor.ofType(String.class)
                .whenVisitingType(Museum.class).then(museum -> "Visited museum: " + museum.getName())
                .whenVisitingType(Painting.class).then(painting -> "Visited painting: " + painting.name())
                .whenVisitingType(Painter.class).then(painter -> "Visited painter: " + painter.getFirstName())
                .whenVisitingType(Person.class).then(person -> "Visited person: " + person.getName())
                .build();
    }

    @NotNull
    private static Museum anyMuseum(Collection<Museum> musea, Predicate<Museum> selector) {
        return musea.stream()
                .filter(selector)
                .findAny()
                .orElseThrow();
    }

    private static boolean isVanGoghMuseum(Museum m) {
        final var name = m.getName();
        return name != null && name.contains("Gogh");
    }

}
