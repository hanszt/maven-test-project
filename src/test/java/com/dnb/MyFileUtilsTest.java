package com.dnb;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MyFileUtilsTest {

    private static Predicate<Path> doesNotContain(String string) {
        return path -> !path.toFile().getPath().contains(string);
    }

    @Test
    void testFindFilesWithDuplicateContent() {
        final var filesWithDuplicateContent = MyFileUtils.findFilesWithDuplicateContent(
                Paths.get(""),
                doesNotContain(".git"),
                doesNotContain("target"),
                doesNotContain("sonarlint"));
        assertTrue(filesWithDuplicateContent.isEmpty(), () -> errorMessage(filesWithDuplicateContent.values()));
    }

    private String errorMessage(Collection<List<Path>> values) {
        return values.stream().mapToInt(List::size).sum() + " duplicate files found: " + values;
    }
}
