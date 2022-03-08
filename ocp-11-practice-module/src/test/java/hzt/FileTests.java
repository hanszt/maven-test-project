package hzt;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

class FileTests {
    // Instances of the File class are immutable; that is, once created,
// the abstract pathname represented by a File object will never change.
    //q29 test 6
    @Test
    void testCreateFileWithFileClass() throws IOException {
        assertFalse(new File("iotests2").createNewFile());
    }
}
