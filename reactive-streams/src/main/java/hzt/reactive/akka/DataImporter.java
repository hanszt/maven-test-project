package hzt.reactive.akka;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static org.hzt.utils.It.println;

public class DataImporter {

    private final ActorSystem actorSystem;
    private final AverageRepository averageRepository = new AverageRepository();

    public DataImporter(ActorSystem actorSystem) {
        this.actorSystem = actorSystem;

    }

    private static List<Integer> parseLine(String line) {
        String[] fields = line.split(";");
        return Arrays.stream(fields)
                .map(Integer::parseInt)
                .toList();
    }

    private static Flow<String, Integer, NotUsed> parseContent() {
        return Flow.of(String.class).mapConcat(DataImporter::parseLine);
    }

    private static Flow<Integer, Double, NotUsed> computeAverage() {
        return Flow.of(Integer.class).grouped(2).mapAsyncUnordered(8, integers ->
                CompletableFuture.supplyAsync(() -> integers
                        .stream()
                        .mapToDouble(v -> v)
                        .average()
                        .orElse(-1.0)));
    }

    private static Flow<String, Double, NotUsed> calculateAverage() {
        return Flow.of(String.class)
                .via(parseContent())
                .via(computeAverage());
    }

    private Sink<Double, CompletionStage<Done>> storeAverages() {
        return Flow.of(Double.class)
                .mapAsyncUnordered(4, averageRepository::save)
                .toMat(Sink.ignore(), Keep.right());
    }


    CompletionStage<Done> calculateAverageForContent(String content) {
        return Source.single(content)
                .via(calculateAverage())
                .runWith(storeAverages(), Materializer.createMaterializer(actorSystem))
                .whenComplete(DataImporter::printResult);
    }

    private static void printResult(Done done, Throwable throwable) {
        if (done != null) {
            println("Import finished ");
        } else {
            throwable.printStackTrace();
        }
    }
}
