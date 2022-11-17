package hzt.reactive.reactor;

import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReactorTests {

    @Test
    void testFluxFromPredefinedElements() {
        final var disposable = Flux.just(1, 2, 3, 4, 5)
                .log()
                .subscribe(System.out::println);

        assertTrue(disposable.isDisposed());
    }

    @Test
    void testGenerateFluxFromIterable() {
        final var mono = Flux.fromIterable(Sequence.iterate(0, i -> ++i))
                .take(10)
                .collect(Collectors.toList());

        assertEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), mono.block());
    }

    @Test
    void testGenerateConnectableFlux() {
        final var connectableFlux = Flux.interval(Duration.ofMillis(500))
                .map(String::valueOf)
                .publish();

        final var subscribe = connectableFlux
                .subscribe(System.out::println);

        connectableFlux.connect();

        assertFalse(subscribe.isDisposed());
    }

    @Test
    void testTimedFlux() {
        Instant now = Instant.now();
        ConnectableFlux<Long> publish = Flux.create((FluxSink<Long> fluxSink) -> defineFluxSink(fluxSink, now))
                .sample(Duration.ofMillis(20))
                .publish();

        publish.subscribe(System.out::println);
        publish.subscribe(System.out::println);

        final var disposable = publish.connect();

        assertFalse(disposable.isDisposed());
    }

    private void defineFluxSink(FluxSink<Long> fluxSink, Instant now) {

        while(Instant.now().toEpochMilli() - now.toEpochMilli() < 1_000) {
            fluxSink.next(System.currentTimeMillis());
        }
    }
}
