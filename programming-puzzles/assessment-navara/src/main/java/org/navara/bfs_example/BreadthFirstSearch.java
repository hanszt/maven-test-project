package org.navara.bfs_example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.navara.utils.MessageConsumer;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @see <a href="https://www.baeldung.com/java-breadth-first-search">Breadt first search Baeldung</a>
 */
public final class BreadthFirstSearch {

    private static final Logger LOGGER = LogManager.getLogger(BreadthFirstSearch.class);

    private final MessageConsumer messageConsumer;

    private BreadthFirstSearch(final MessageConsumer consumer) {
        this.messageConsumer = consumer;
    }

    public static BreadthFirstSearch withMessageConsumer(final Consumer<String> stringConsumer) {
        return new BreadthFirstSearch(new MessageConsumer(stringConsumer));
    }

    public static BreadthFirstSearch buildWithLogging() {
        return new BreadthFirstSearch(new MessageConsumer(LOGGER::info));
    }

    public static BreadthFirstSearch build() {
        return new BreadthFirstSearch(new MessageConsumer(stringSupplier -> {}));
    }

    public <T> Optional<Node<T>> search(final T value, final Node<T> start) {
        messageConsumer.accept("Start searching...");
        final Set<Node<T>> alreadyVisited = new HashSet<>();
        final Queue<Node<T>> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            final Node<T> currentNode = queue.remove();
            messageConsumer.accept(() -> "Visited node with value: %s".formatted(currentNode.getValue()));

            if (currentNode.getValue().equals(value)) {
                messageConsumer.accept(() -> "%s found".formatted(currentNode));
                return Optional.of(currentNode);
            } else {
                alreadyVisited.add(currentNode);
                queue.addAll(currentNode.getNeighbors());
                queue.removeAll(alreadyVisited);
            }
        }
        messageConsumer.accept("Search ended without result");
        return Optional.empty();
    }

    public <T> Optional<Tree<T>> search(final T value, final Tree<T> start) {
        final Queue<Tree<T>> queue = new ArrayDeque<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            final Tree<T> currentNode = queue.remove();
            messageConsumer.accept(() -> "Visited node with value: %s".formatted(currentNode.getValue()));

            if (currentNode.getValue().equals(value)) {
                return Optional.of(currentNode);
            } else {
                queue.addAll(currentNode.getChildren());
            }
        }
        return Optional.empty();
    }

    public static void main(final String[] args) {
        final Node<Integer> start = Node.of(10);
        final Node<Integer> firstNeighbor = Node.of(2);
        start.connect(firstNeighbor);

        final Node<Integer> firstNeighborNeighbor = Node.of(3);
        firstNeighbor.connect(firstNeighborNeighbor);
        firstNeighborNeighbor.connect(start);

        final Node<Integer> secondNeighbor = Node.of(4);
        start.connect(secondNeighbor);

        final var result = BreadthFirstSearch
                .buildWithLogging()
                .search(4, firstNeighbor)
                .orElseThrow();

        LOGGER.info(result);
    }
}
