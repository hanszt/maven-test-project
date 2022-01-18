package hzt.reactive.akka;

import akka.actor.ActorSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DataImporterTest {

    private final ActorSystem actorSystem = ActorSystem.create();

    @Test
    void givenStreamOfIntegers_whenCalculateAverageAndSaveToSink_thenShouldFinishSuccessfully() {
        //given
        DataImporter dataImporter = new DataImporter(actorSystem);
        String input = "10;90;110;10";

        //when
        assertDoesNotThrow(() -> {
            dataImporter.calculateAverageForContent(input)
                    .thenApplyAsync(d -> actorSystem.terminate());
        });
    }
}
