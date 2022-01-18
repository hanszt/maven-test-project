package org.hzt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static java.util.stream.Collectors.joining;

public class RemovingWhiteSpacesAndReturns {

    String removeSpacesFromLinesInTextFile(String fileName) throws IOException {
        try (var lines = Files.lines(new File(fileName).toPath())) {
            return lines.map(String::strip).collect(joining());
        }
    }
}
