package hzt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.System.out;
import static java.util.function.Predicate.not;

@SuppressWarnings("SameParameterValue")
public class StreamSplitting {

    private static final Pattern SENTENCE_SPLITTER = Pattern.compile("[ ,.!?\r\n]");
    public static void main(String... args) {
        StreamSplitting streamSplitting = new StreamSplitting();
        out.println("Partitioning by:");
        streamSplitting.partitioningBy(10, 15, x -> x % 2 == 0)
                .entrySet().forEach(StreamSplitting::printEntry);
        out.println();
        out.println("Grouping by: ");
        streamSplitting.groupingIntegers(10, 55, x -> x % 6)
                .entrySet()
                .forEach(StreamSplitting::printEntry);
        streamSplitting.flatMap();
        out.println("distinct words in a sentence");
        List<String> sentences = List.of(
                "hallo dit is een zin",
                "Dit, is nog een rare zin",
                "This sentence makes no sense  zin ? ");
        streamSplitting.distinct(sentences).forEach(out::println);

        out.println("streamSplitting.chainedLambda() = " + streamSplitting.chainedLambda());
    }

    private int chainedLambda() {
        IntFunction<IntUnaryOperator> intFunction = a -> b -> a - b;
        return operate(intFunction.apply(20));
    }

    private List<String> distinct(List<String> sentences) {
        return sentences.stream()
                .flatMap(SENTENCE_SPLITTER::splitAsStream)
                .filter(not(String::isEmpty))
                .distinct()
                .collect(Collectors.toList());
    }

    private void flatMap() {
        out.println("Flatmap: ");
        List<String> list1 = List.of("a", "b");
        List<String> list2 = List.of("1", "2");
        Stream.of(list1, list2)
                .flatMap(Collection::stream)
                .forEach(out::println);
        out.println();
    }

    private Map<Integer, List<Integer>> groupingIntegers(int startInclusive,
                                                         int endInclusive,
                                                         UnaryOperator<Integer> integerIntegerFunction) {
        return IntStream.rangeClosed(startInclusive, endInclusive)
                .boxed()
                .sorted().collect(Collectors.groupingBy(integerIntegerFunction));
    }

    private static <K, V> void printEntry(Map.Entry<K, V> e) {
        out.printf("key: %2s, value: %s%n", e.getKey(), e.getValue());
    }

    private Map<Boolean, List<Integer>> partitioningBy(int startInclusive,
                                                       int endInclusive,
                                                       Predicate<Integer> integerPredicate) {
        return IntStream.rangeClosed(startInclusive, endInclusive)
                .boxed()
                .collect(Collectors.partitioningBy(integerPredicate));
    }

    public static int operate(IntUnaryOperator intUnaryOperator) {
        return intUnaryOperator.applyAsInt(5);
    }

}
