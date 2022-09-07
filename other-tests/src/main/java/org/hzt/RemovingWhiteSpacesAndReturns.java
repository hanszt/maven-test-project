package org.hzt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.stream.Collectors.joining;

public class RemovingWhiteSpacesAndReturns {

    String removeSpacesFromLinesInTextFile(String fileName) throws IOException {
        try (var lines = Files.lines(Path.of(fileName))) {
            return lines
                    .map(String::strip)
                    .collect(joining());
        }
    }
}
