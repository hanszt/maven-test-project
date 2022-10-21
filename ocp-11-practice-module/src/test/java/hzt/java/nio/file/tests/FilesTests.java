package hzt.java.nio.file.tests;

import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.FileVisitResult.CONTINUE;
import static org.junit.jupiter.api.Assertions.*;

class FilesTests {

    @Test
    void testFilesFind() throws IOException {
        final var maxSearchDepth = Integer.MAX_VALUE;
        try (var fileContent = Files
                .find(Path.of("iotests\\pathtest"), maxSearchDepth, this::isRegularFileEndingWithTestTxt)) {

            final var result = fileContent
                    .map(this::readContentAsString)
                    .findFirst()
                    .orElseThrow();

            assertEquals("Gevonden!", result.strip());
        }
    }

    @Test
    void testReadString() throws IOException {
        final var string = Files.readString(Path.of("./pom.xml"));
        System.out.println(string);

        final var lineCount = string.lines().count();

        assertEquals(54, lineCount);
    }

    private String readContentAsString(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean isRegularFileEndingWithTestTxt(Path path, BasicFileAttributes attributes) {
        return path.endsWith("test.txt") && attributes.isRegularFile();
    }

    //    q21 test 3

    @Test
    void testDeleteIfExists() throws IOException {
        Path path = Paths.get("iotests\\out");

        var deleted = Files.deleteIfExists(path);
        assertFalse(deleted);
    }

    @Test
    void testFilesLastModifiedTimeOf() throws IOException {
        final var lastModifiedTime = Files.getLastModifiedTime(Path.of("./pom.xml"));

        final var dateTime = lastModifiedTime.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        System.out.println(dateTime);

        assertEquals(2022, dateTime.getYear());
    }

    @Test
    void testWalkSourceFileTree() throws IOException {
        final List<Path> regularFiles = new ArrayList<>();
        final List<Path> directories = new ArrayList<>();
        final var start = Path.of("./src");
        final var path = Files.walkFileTree(start, new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
                if (attrs.isRegularFile()) {
                    regularFiles.add(path);
                }
                return CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                directories.add(dir);
                return CONTINUE;
            }
        });
        System.out.println("path.toAbsolutePath() = " + path.toAbsolutePath());

        assertAll(
                () -> assertEquals(start, path),
                () -> assertEquals(94, regularFiles.size()),
                () -> assertEquals(35, directories.size())
        );
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

        System.out.println(source);
        System.out.println(targetDir);

        assertAll(
                () -> assertEquals(source.getName(0), targetDir.getName(0)),
                () -> assertEquals(source.getParent(), targetDir.getParent())
        );
    }

}
