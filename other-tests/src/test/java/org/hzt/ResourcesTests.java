package org.hzt;

import org.hzt.test.ReplaceCamelCaseBySentence;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayNameGeneration(ReplaceCamelCaseBySentence.class)
class ResourcesTests {

    @Test
    void testResourceContentByInputStream() throws IOException {
        final var NAME = "/input.txt";
        try (final var inputStream = ResourcesTests.class.getResourceAsStream(NAME);
             final var resourceAsStream = Objects.requireNonNull(inputStream, "Could not find resource " + NAME);
             final var inputStreamReader = new InputStreamReader(resourceAsStream);
             final var bufferedReader = new BufferedReader(inputStreamReader);

             final var lines = bufferedReader.lines()) {
            final var joinedLines = lines.collect(Collectors.joining());
            assertEquals("This is some input", joinedLines);
        }
    }

    @Test
    void testResourceContentByUrlToPath() throws IOException {
        final var NAME = "/input.txt";
        final var path = Optional.ofNullable(ResourcesTests.class.getResource(NAME))
                .map(URL::getFile)
                .map(File::new)
                .map(File::toPath)
                .orElseThrow(() -> new IllegalStateException("Could not find resource " + NAME));

        try (final var lines = Files.lines(path)) {
            final var joinedLines = lines.collect(Collectors.joining());
            assertEquals("This is some input", joinedLines);
        }
    }
}
