package org.hzt;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @see <a href="https://www.baeldung.com/java-kafka-streams">Introduction to KafkaStreams in Java</a>
 */
class KafkaStreamSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaStreamSample.class);

    public static void main(String[] args) {
        LOGGER.info("Kafka sample started");
        try {
            final var properties = configureProperties();
            countNrOfWordsUsingKafkaStreamAndPrint(properties);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            LOGGER.error("An IO exception occurred", e);
        }
    }

    private static Properties configureProperties() throws IOException {
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-live-test");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        final var name = Serdes.String().getClass().getName();
        streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, name);
        streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, name);
        Path stateDirectory = Files.createTempDirectory("kafka-streams");
        LOGGER.info("temp directory: {}", stateDirectory);
        streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, stateDirectory.toAbsolutePath().toString());
        return streamsConfiguration;
    }

    private static void countNrOfWordsUsingKafkaStreamAndPrint(Properties streamsConfiguration) throws InterruptedException {
        Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream("Hallo");

        KTable<String, Long> wordCounts = textLines
                .flatMapValues(value -> List.of(pattern.split(value.toLowerCase())))
                .groupBy((key, word) -> word)
                .count();

        wordCounts.toStream().foreach(KafkaStreamSample::printWordToCount);

        String outputTopic = "outputTopic";
        wordCounts.toStream()
                .to(outputTopic, Produced.with(Serdes.String(), Serdes.Long()));

        Topology topology = builder.build();
        try (KafkaStreams streams = new KafkaStreams(topology, streamsConfiguration)) {
            streams.start();
            Thread.sleep(500);
        }
    }

    private static void printWordToCount(String word, Long count) {
        LOGGER.info("word: {} -> {}", word, count);
    }
}
