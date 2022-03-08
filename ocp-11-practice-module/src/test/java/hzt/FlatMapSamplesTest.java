package hzt;

import hzt.FlatMapSamples;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Comparator.naturalOrder;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FlatMapSamplesTest {

    private final FlatMapSamples flatMapSamples = new FlatMapSamples();

    @Test
    void testStreamFlatMapSampleTest() {
        String a = "A";
        String b = "B";
        List<String> result = flatMapSamples.streamOfStreamsToList(Stream.of(Stream.of(a), Stream.of(b)));
        assertEquals(List.of(a, b), result);
    }

    @Test
    void testListFlatMapSampleTest() {
        String a = "A";
        String b = "B";
        List<String> result = flatMapSamples.collectionOfCollectionsToList(List.of(List.of(a), Set.of(b)));
        assertEquals(List.of(a, b), result);
    }

    @Test
    void testStream() {
        String a = "A";
        String b = "B";
        var result = Stream.of(List.of(a), Set.of(b, "sdfsBc", "c"))
                .flatMap(Collection::stream)
                .map(String::strip)
                .filter(string -> string.contains(b))
                .sorted(naturalOrder())
                .dropWhile(string -> string.equals("c"))
                .takeWhile(not(String::isEmpty))
                .distinct()
                .collect(joining());

        assertEquals("BsdfsBc", result);
    }

}
