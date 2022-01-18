package hzt.reactive;

import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.JavaFlowSupport;
import io.reactivex.rxjava3.core.Flowable;
import org.reactivestreams.FlowAdapters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.adapter.JdkFlowAdapter;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Publisher;

/**
 *
 * Gluing different implementations of Reactive streams using the Reactive Streams api from java 9+
 * @see <a href="https://youtu.be/_stAxdjx8qk?t=2406">Reactive Streams in Java 9+, use external libs</a>
 * @see <a href="https://youtu.be/kG2SEcl1aMM?t=4487">
 *     Java Streams vs Reactive Streams: Which, When, How, and Why? by Venkat Subramaniam</a>
 */
public class IntegrationApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationApp.class);

    public static void main(String[] args) {
        var reactorPublisher = reactorPublisher();
        var akkaStreamsProcessor = akkaStreamProcessor();
        reactorPublisher.subscribe(akkaStreamsProcessor);
        final var disposable = Flowable
                .fromPublisher(FlowAdapters.toProcessor(akkaStreamsProcessor))
                .subscribe(result -> LOGGER.info("{}", result));
        LOGGER.info("Is disposed: {}", disposable.isDisposed());
    }

    private static Publisher<Long> reactorPublisher() {
        final var period = Duration.ofSeconds(1);
        final var interval = Flux.interval(period);
        return JdkFlowAdapter.publisherToFlowPublisher(interval);
    }

    private static Processor<Long, Long> akkaStreamProcessor() {
        var negatingFlow = Flow.of(Long.class).map(i -> -i);
        final var materializer = Materializer.createMaterializer(ActorSystem.create());
        return JavaFlowSupport.Flow.toProcessor(negatingFlow).run(materializer);
    }

}
