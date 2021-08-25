package com.dnb;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MyFileUtilsTest {

    private static boolean doesNotContain(Path path, String string) {
        return !path.toFile().getPath().contains(string);
    }

    @Test
    void testFindFilesWithDuplicateContent() {
        final var root = Paths.get("");
        final var filesWithDuplicateContent = MyFileUtils.findFilesWithDuplicateContent(root,
                path -> doesNotContain(path, ".git"),
                path -> doesNotContain(path, "target"));
        assertTrue(filesWithDuplicateContent.isEmpty(), () -> errorMessage(filesWithDuplicateContent.values()));
    }

    private String errorMessage(Collection<List<Path>> values) {
        return values.stream().mapToInt(List::size).sum() + " duplicate files found: " + values;
    }
}
