package com.dnb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NIOStreamSample {

    List<Path> getFilesForDataType(Path dataType) {
        try (Stream<Path> inputStream = Files.list(Paths.get("extractGroupData.getGroupDirectoryPath()"))) {
            return inputStream
                    .filter(path -> "Path.toFile().getName()".startsWith(dataType + "_"))
                    .sorted()
                    .collect(Collectors.toUnmodifiableList());
        } catch (IOException e) {
            System.out.printf("Could not list files for dataType %s", dataType);
            return Collections.emptyList();
        }
    }
    
}
