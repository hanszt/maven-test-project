package hzt.java.nio.file.tests;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class FilesTests {

    @Test
    void testFilesFind() {
        final var maxSearchDepth = Integer.MAX_VALUE;
        try (var fileContent = Files.find(Path.of("iotests\\pathtest"),
                maxSearchDepth,
                this::findCriteria)) {

            final var result = fileContent
                    .map(this::readContentAsString)
                    .findFirst()
                    .orElseThrow();

            assertEquals("Gevonden!", result.strip());
        } catch (IOException e) {
            fail("Failed because of " + e);
        }
    }

    private String readContentAsString(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean findCriteria(Path path, BasicFileAttributes attributes) {
        return path.endsWith("test.txt") && attributes.isRegularFile();
    }

    //    q21 test 3

    @Test
    void testDeleteIfExists() {
        Path path = Paths.get("iotests\\out");
        try {
            var deleted = Files.deleteIfExists(path);
            assertFalse(deleted);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDelete() {
        Path path = Paths.get("iotests\\out");
        assertThrows(NoSuchFileException.class, () -> Files.delete(path));
    }

    //q 28 test 3
//    From the JavaDoc API documentation for Files.copy:
//    Attempts to copy the file attributes associated with source file to the target file.
//    The exact file attributes that are copied is platform and file system dependent and therefore unspecified.
//    Minimally, the last-modified-time is copied to the target file if supported by both the source and target file store.
//    Copying of file timestamps may result in precision loss.

    @Test
    void testFilesCopy() {
        Path sourcePath = Paths.get("iotests\\test1.txt");
        Path targetPath = Paths.get("iotests\\test2.txt");
        try {
            System.out.println(targetPath + " exists: " + Files.deleteIfExists(targetPath));
            final Path pathToTarget = Files.copy(sourcePath, targetPath,
                    StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING);

            assertEquals(targetPath, pathToTarget);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    void testFilesIsSameFile() throws IOException {
        Path sourcePath = Paths.get("iotests\\test1.txt");
        Path targetPath = Paths.get("iotests\\test2.txt");
        assertFalse(Files.isSameFile(sourcePath, targetPath));
    }

    //    q45 test 2
    @Test
    void testPathResolveMethod() {
        var p1 = Paths.get("\\temp\\records");
        var p2 = p1.resolve("clients.dat");

        System.out.println("p2 = " + p2);

        final var isValid = p2.startsWith("\\temp") && p2.endsWith("clients.dat");

        assertTrue(isValid);
    }

    @Test
    void testPathResolveSibling() throws IOException {
        var source = Paths.get("iotests\\test1.txt");
        var targetDir = source.resolveSibling("text2.txt");

        try (var bw = new BufferedWriter(new FileWriter(targetDir.toFile()))) {
            bw.write("hello");
        }

        assertEquals(source.getName(0), targetDir.getName(0));
    }

}
