package com.dnb.utils;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import static com.dnb.utils.HigherOrderFunctions.asFun;
import static com.dnb.utils.HigherOrderFunctions.by;
import static com.dnb.utils.predicates.StringPredicates.doesntContain;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MyFileUtilsTest {

    private static Predicate<Path> doesNotContain(String string) {
        return by(asFun(Path::toFile).andThen(File::getPath), doesntContain(string));
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
