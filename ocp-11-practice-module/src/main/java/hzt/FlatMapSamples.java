package hzt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.UnaryOperator.identity;

public class FlatMapSamples {

    public <T> List<T> streamOfStreamsToList(Stream<Stream<T>> streamOfStreams) {
        return streamOfStreams
                .flatMap(identity())
                .collect(Collectors.toList());
    }

    public <T> List<T> collectionOfCollectionsToList(Collection<Collection<T>> collectionOfCollections) {
        return collectionOfCollections.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}
