package hzt.java.nio.file.tests;

import hzt.OsAssumptions;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
import java.util.function.Consumer;

import static java.nio.file.FileVisitResult.CONTINUE;
import static org.junit.jupiter.api.Assertions.*;

class FilesTests {

    @Test
    void testFilesFind() throws IOException {
        OsAssumptions.assumeIsWindowsOs();

        final var maxSearchDepth = Integer.MAX_VALUE;
        try (var fileContent = Files
                .find(Path.of("iotests\\pathtest"), maxSearchDepth, FilesTests::isRegularFileEndingWithTestTxt)) {

            final var result = fileContent
                    .map(FilesTests::readContentAsString)
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

    private static String readContentAsString(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static boolean isRegularFileEndingWithTestTxt(Path path, BasicFileAttributes attributes) {
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
                .atZone(ZoneId.of("Europe/Amsterdam"))
                .toLocalDateTime();

        System.out.println(dateTime);

        assertTrue(2022 < dateTime.getYear());
    }

    @Test
    void testWalkSourceFileTree() throws IOException {
        final List<Path> regularFiles = new ArrayList<>();
        final List<Path> directories = new ArrayList<>();
        final var start = Path.of("./src");
        final var path = Files.walkFileTree(start, new ConsumingFileVisitor(regularFiles::add, directories::add));

        System.out.println("path.toAbsolutePath() = " + path.toAbsolutePath());
        System.out.println("regular files size = " + regularFiles.size());
        System.out.println("directories size = " + directories.size());

        assertAll(
                () -> assertEquals(start, path),
                () -> assertFalse(regularFiles.isEmpty()),
                () -> assertFalse(directories.isEmpty())
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
        OsAssumptions.assumeIsWindowsOs();

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
        OsAssumptions.assumeIsWindowsOs();

        Path sourcePath = Paths.get("iotests\\test1.txt");
        Path targetPath = Paths.get("iotests\\test2.txt");
        assertFalse(Files.isSameFile(sourcePath, targetPath));
    }

    //    q45 test 2
    @Test
    void testPathResolveMethod() {
        OsAssumptions.assumeIsWindowsOs();

        var p1 = Paths.get("\\temp\\records");
        var p2 = p1.resolve("clients.dat");

        System.out.println("p2 = " + p2);

        final var isValid = p2.startsWith("\\temp") && p2.endsWith("clients.dat");

        assertTrue(isValid);
    }

    @Test
    void testPathResolveSibling() throws IOException {
        OsAssumptions.assumeIsWindowsOs();

        var source = Paths.get("iotests\\test1.txt");
        var targetDir = source.resolveSibling("text2.txt");

        try (var bw = new BufferedWriter(new FileWriter(targetDir.toFile(), StandardCharsets.UTF_8))) {
            bw.write("hello");
        }

        System.out.println(source);
        System.out.println(targetDir);

        assertAll(
                () -> assertEquals(source.getName(0), targetDir.getName(0)),
                () -> assertEquals(source.getParent(), targetDir.getParent())
        );
    }

    private static final class ConsumingFileVisitor extends SimpleFileVisitor<Path> {

        private final Consumer<Path> regularFileConsumer;
        private final Consumer<Path> directoryConsumer;

        private ConsumingFileVisitor(Consumer<Path> regularFileConsumer, Consumer<Path> directoryConsumer) {
            this.regularFileConsumer = regularFileConsumer;
            this.directoryConsumer = directoryConsumer;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
            if (attrs.isRegularFile()) {
                regularFileConsumer.accept(path);
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
            directoryConsumer.accept(dir);
            return CONTINUE;
        }
    }
}
