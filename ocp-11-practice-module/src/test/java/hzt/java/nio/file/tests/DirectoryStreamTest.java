package hzt.java.nio.file.tests;

import org.junit.jupiter.api.Test;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectoryStreamTest {

    @Test
    void testDirectoryStream() {
        Path dir = Paths.get("iotests\\dir\\pictures");
        // non recursive
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(dir, "*.{png,jpg}")) {
            final var list = StreamSupport.stream(directoryStream.spliterator(), false)
                    .collect(Collectors.toUnmodifiableList());
            assertEquals(2, list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testFilesWalkAsAlternativeForDirectoryStream() {
//        Files.list can also be used. That is non-recursive
        Path dir = Paths.get("iotests");
        // recursive
        try (Stream<Path> paths = Files.walk(dir)) {
            final var list = paths
                    .filter(DirectoryStreamTest::filterPictures)
                    .collect(Collectors.toUnmodifiableList());
            assertEquals(2, list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean filterPictures(Path p) {
        final var fileName = p.toFile().getName();
        final var png = fileName.endsWith("png");
        return png || fileName.endsWith("jpg");
    }
}
