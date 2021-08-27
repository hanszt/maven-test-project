package com.dnb;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MyFileUtils {

    private MyFileUtils() {
    }

    /**
     * @param start the start location in the file tree to walk along al files down
     * @param pathFilters optional extra filters for the paths found during file walking
     * @return a map which has as key the duplicate message and as value a list with the paths to the duplicate messages
     */
    @SafeVarargs
    public static Map<String, List<Path>> findFilesWithDuplicateContent(Path start, Predicate<Path>... pathFilters) {
        Predicate<Path> combinedPathFilter = HigherOrderFunctions.allMatch(pathFilters);
        try (final Stream<Path> walk = Files.walk(start)) {
            return walk.filter(path -> path.toFile().isFile())
                    .filter(combinedPathFilter)
                    .collect(Collectors.groupingBy(MyFileUtils::loadFileAsTrimmedString))
                    .entrySet()
                    .stream()
                    .filter(e -> e.getValue().size() > 1)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (IOException e) {
            throw new IllegalStateException(start + " not found...", e);
        }
    }

    public static String loadFileAsTrimmedString(Path path) {
        try (var lines = Files.lines(path, StandardCharsets.ISO_8859_1)) {
            return lines.map(String::strip)
                    .collect(Collectors.joining());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

    }
}
