package hzt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Stream;

import static java.lang.System.getProperty;
import static org.junit.jupiter.api.Assertions.*;

class FileTests {
    // Instances of the File class are immutable; that is, once created,
    // the abstract pathname represented by a File object will never change.
    //q29 test 6
    @Test
    void testCreateFileWithFileClass() throws IOException {
        assertFalse(new File("iotests2").createNewFile());
    }

    @Test
    void testMkDirDoesNotCreateDirStructure() {
        final var file = new File("./testDir/secondDir");
        final var isMade = file.mkdir();
        assertFalse(isMade);
    }

    @Test
    void testMkDirsDoesCreateDirStructure() {
        final var file = new File("./testDir/secondDir");
        final var isMade = file.mkdirs();
        final var deleted = file.delete();
        final var parentDeleted = new File("./testDir").delete();

        assertAll(
                () -> assertTrue(deleted),
                () -> assertTrue(isMade),
                () -> assertTrue(parentDeleted)
        );
    }

    @Test
    void testCreateParentStream() {
        final var java = Stream.iterate(new File(getProperty("user.dir")), Objects::nonNull, File::getParentFile)
                .filter(file -> file.getName().toLowerCase().contains("java"))
                .findFirst()
                .orElseThrow();

        assertEquals("C:\\_Programming\\Java", java.getAbsolutePath());
    }

    @ParameterizedTest
    @ValueSource(strings = {"file1", "file2"})
    void testImplicitConversionToFile(File file) {
        assertFalse(file.exists());
    }

}
