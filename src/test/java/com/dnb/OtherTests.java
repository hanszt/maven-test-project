package com.dnb;

import com.dnb.model.Bic;
import com.dnb.model.Book;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.IntStream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class OtherTests {

    @Test
    void testChars() {
        IntStream.range(33, 900).mapToObj(OtherTests::toCharacter).forEach(System.out::println);
        System.out.println();
        "Hello".chars().mapToObj(OtherTests::toCharacter).forEach(System.out::println);
        String.format("Hello,%nI'm Hans").lines().forEach(System.out::printf);
        char c = 'ͽ';
        assertEquals(893, c);
    }

    private static Character toCharacter(int i) {
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
    void testSetBehaviourContainingNull() {
        Set<String> stringSet = new HashSet<>();
        stringSet.add(null);
        assertTrue(stringSet.remove(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"bm", "Bm", "BM", "bM"})
    void testEqualsIgnoreCase(String stringToCompare) {
        assertTrue("BM".equalsIgnoreCase(stringToCompare));
    }

    @Test
    void testGenericsMethodToString() {
        var opt = asOptional("Hallo");
        opt.ifPresentOrElse(e -> assertEquals("Hallo", e), () -> fail("Not present"));
    }

    @Test
    void testGenericsMethodToBic() {
        final var bic = new Bic("Hallo");
        var opt = asOptional(bic);
        opt.ifPresentOrElse(e -> assertEquals(bic, e), () -> fail("Not present"));
    }

    private static <T> Optional<T> asOptional(T value) {
        return Optional.ofNullable(value);
    }

    @Test
    void testGroupingBy() {
        List<String> strings = List.of("hallo", "hoe", "is", "het");

        var groupedByMap = strings.stream()
                .collect(groupingBy(String::length));
        var wordsOfLengthThreeList = groupedByMap.get(3);
        assertEquals(2, wordsOfLengthThreeList.size());
        assertTrue(wordsOfLengthThreeList.containsAll(Set.of("hoe", "het")));
    }

    @Test
    void testPartitioningBy() {
        List<String> strings = List.of("hallo", "hoe", "is", "het");

        var partitionedByMap = strings.stream()
                .collect(partitioningBy(string -> string.contains("e")));
        final var containsEList = partitionedByMap.get(true);
        assertEquals(2, containsEList.size());
        assertTrue(containsEList.containsAll(Set.of("hoe", "het")));
    }

    @Test
    void creatingBathes() {
        List<String> data = IntStream.range(0, 950).mapToObj(i -> "item " + i).collect(toList());

        final int BATCH_SIZE = 100;

        List<List<String>> batches = IntStream.range(0, (data.size() + BATCH_SIZE - 1) / BATCH_SIZE)
                .mapToObj(i -> data.subList(i * BATCH_SIZE, Math.min(data.size(), (i + 1) * BATCH_SIZE)))
                .collect(toList());

        assertEquals(10, batches.size());
        printBatches(batches);
    }

    private void printBatches(List<List<String>> batches) {
        int counter = 0;
        for (List<String> batch : batches) {
            System.out.println("Batch " + counter);
            System.out.println("batch = " + batch);
            System.out.println();
            counter++;
        }
    }

    @Test
    void testThrowable() {
        final var throwable = new Throwable();
        System.out.println("throwable.getLocalizedMessage() = " + throwable.getLocalizedMessage());
        assertNull(throwable.getMessage());
    }

    @Test
    void testTreeSetWithComparator() {
        SortedSet<Book> set = new TreeSet<>(comparing(Book::getBookName));
        final var book = new Book("");
        final var book2 = new Book("");
        book.setBookName("Hallo");
        book2.setBookName("Test");
        set.addAll(Set.of(book, book2));
        assertEquals(book2, set.last());
    }

    @Test
    void testObjectRequireNonNullElseAndElseGet() {
        Integer integer = Objects.requireNonNullElse(null, 4);
        assertEquals(4, integer);
        Integer integer2 = Objects.requireNonNullElseGet(null, () -> 4);
        assertEquals(4, integer2);
    }
}
