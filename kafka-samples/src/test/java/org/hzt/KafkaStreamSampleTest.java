package org.hzt;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KafkaStreamSampleTest {

    private static final String DO_NOT_USE_THREAD_SLEEP_IN_TESTS = "java:S2925";

    @Test
    @SuppressWarnings(DO_NOT_USE_THREAD_SLEEP_IN_TESTS)
    void testKafkaStreamMap() throws InterruptedException {
        final Properties streamsConfiguration = createStreamConfig();

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream("Hallo");
        Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);

        KTable<String, Long> wordCounts = textLines
                .flatMapValues(word -> List.of(pattern.split(word.toLowerCase())))
                .groupBy((key, word) -> word)
                .count();
        record WordToCount(String word, long count) {
        }
        final var wordToCounts = new ArrayList<WordToCount>();
        wordCounts.toStream().foreach((word, count) -> wordToCounts.add(new WordToCount(word, count)));

        String outputTopic = "outputTopic";
        wordCounts.toStream().to(outputTopic, Produced.with(Serdes.String(), Serdes.Long()));

        Topology topology = builder.build();
        try (KafkaStreams streams = new KafkaStreams(topology, streamsConfiguration)) {
            streams.start();
        }
        TimeUnit.MILLISECONDS.sleep(500);

        assertEquals(0, wordToCounts.size());
    }

    private static Properties createStreamConfig() {
        final Properties streamsConfiguration = new Properties();
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-live-test");
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        try (final var string = Serdes.String()) {
            final var name = string.getClass().getName();
            streamsConfiguration.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, name);
            streamsConfiguration.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, name);

            Path stateDirectory = Files.createTempDirectory("kafka-streams");
            System.out.println("stateDirectory = " + stateDirectory);
            streamsConfiguration.put(StreamsConfig.STATE_DIR_CONFIG, stateDirectory.toAbsolutePath().toString());
            return streamsConfiguration;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
