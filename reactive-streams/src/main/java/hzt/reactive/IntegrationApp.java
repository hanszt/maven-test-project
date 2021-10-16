package hzt.reactive;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.JavaFlowSupport;
import io.reactivex.rxjava3.core.Flowable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.FlowAdapters;
import reactor.adapter.JdkFlowAdapter;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Publisher;

/**
 *
 * Gluing different implementations of Reactive streams using the Reactive Streams api from java 9+
 * https://youtu.be/_stAxdjx8qk?t=2406
 * https://youtu.be/kG2SEcl1aMM?t=4487
 */
public class IntegrationApp {

    private static final Logger LOGGER = LogManager.getLogger(IntegrationApp.class);

    private static final ActorSystem actorSystem = ActorSystem.create();
    private static final ActorMaterializer actorMaterializer = ActorMaterializer.create(actorSystem);

    public static void main(String[] args) {
        var reactorPublisher = reactorPublisher();
        var akkaStreamsProcessor = akkaStreamProcessor();
        reactorPublisher.subscribe(akkaStreamsProcessor);
        Flowable
                .fromPublisher(FlowAdapters.toProcessor(akkaStreamsProcessor))
                .subscribe(LOGGER::info);
    }

    private static Publisher<Long> reactorPublisher() {
        final var period = Duration.ofSeconds(1);
        final var interval = Flux.interval(period);
        return JdkFlowAdapter.publisherToFlowPublisher(interval);
    }

    private static Processor<Long, Long> akkaStreamProcessor() {
        var negatingFlow = Flow.of(Long.class)
                .map(i -> -i);
        return JavaFlowSupport.Flow.toProcessor(negatingFlow).run(actorMaterializer);
    }

}
