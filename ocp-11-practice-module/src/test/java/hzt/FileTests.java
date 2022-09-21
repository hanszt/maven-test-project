package hzt;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
