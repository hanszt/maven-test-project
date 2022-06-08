package org.hzt;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

class KafkaStreamSampleTest {

    private static final String INPUT_TOPIC = "inputTopic";

    private final Properties streamsConfiguration = new Properties();

    @BeforeEach
    void setup() throws IOException {
        streamsConfiguration.put(
                StreamsConfig.APPLICATION_ID_CONFIG,
                "wordcount-live-test");
        String bootstrapServers = "localhost:9092";
        streamsConfiguration.put(
                StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        final var name = Serdes.String().getClass().getName();
        streamsConfiguration.put(
                StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG,
                name);
        streamsConfiguration.put(
                StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG,
                name);
        Path stateDirectory = Files.createTempDirectory("kafka-streams");
        streamsConfiguration.put(
                StreamsConfig.STATE_DIR_CONFIG, stateDirectory.toAbsolutePath().toString());

    }

    @Test
    void testKafkaStreamMap() throws InterruptedException {
        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> textLines = builder.stream("Hallo");
        Pattern pattern = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS);

        KTable<String, Long> wordCounts = textLines
                .flatMapValues(value -> Arrays.asList(pattern.split(value.toLowerCase())))
                .groupBy((key, word) -> word)
                .count();

        wordCounts.toStream().foreach(this::printWordToCount);

        String outputTopic = "outputTopic";
        wordCounts.toStream()
                .to(outputTopic, Produced.with(Serdes.String(), Serdes.Long()));

        Topology topology = builder.build();
        KafkaStreams streams = new KafkaStreams(topology, streamsConfiguration);
        streams.start();

        Thread.sleep(500);
        streams.close();

       assertTrue(pattern.matcher(" ").matches());
    }

    private void printWordToCount(String word, Long count) {
        System.out.println("word: " + word + " -> " + count);
    }

}
