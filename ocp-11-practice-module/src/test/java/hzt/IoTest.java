package hzt;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IoTest {

    //    Source: Enthuware Java11OCP Test 1 Q 25
    //    NoSuchFileException and AccessDeniedException are subclasses of IOException.
    //    You cannot include classes that are related by inheritance in the same multi-catch block.
    //    Remember that BufferedReader.close (which is called automatically at the end of the try-with-resources block)
    //    and BufferedReader.readLine methods throw java.io.IOException.
    @Test
    void testPrivileged() {
        try (BufferedReader bfr = new BufferedReader(new FileReader("src/test/java/hzt/IoTest.java"))) {
            String line;
            while ((line = bfr.readLine()) != null) {
                System.out.println(line);
                assertNotNull(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Source: Enthuware Java11OCP Test 1 Q 6
    @Test
    void testMarkedBufferedReader() {
        final var strings = IoSamples.bufferedReaderWithMarker("test.txt");
        assertEquals(List.of("A", "B", "C", "B", "B"), strings);
    }

    @Test
    void testExecutePrivilegedFunction() {
        try (final var walk = Files.walk(Path.of("src/main/java"))) {

            var files = walk
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .toList();

            final var strings = new IoSamples().executePrivilegedFunction(files, File::toString);
            strings.forEach(System.out::println);
            strings.forEach(s -> assertFalse(s.isEmpty()));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // Source: Enthuware Java11OCP Test 1 Q 6
    @Test
    void testWriteFile() throws IOException {
        try (var fw = new FileWriter("text.txt")) {
            fw.write("hello");
            assertTrue(new File("text.txt").exists());
        }
    }

    //q 32 test 3
    //The permitted values for the access mode and their meanings are:
    //
    //"r": Open for reading only. Invoking any of the write methods of the resulting object will cause an IOException to be thrown.
    //"rw": Open for reading and writing. If the file does not already exist then an attempt will be made to create it.
    //"rws": Open for reading and writing, as with "rw", and also require that every update to the file's content or metadata be written synchronously to the underlying storage device.
    //"rwd": Open for reading and writing, as with "rw", and also require that every update to the file's content be written synchronously to the underlying storage device.
    //
    //
    @Test
    void testRandomAccessFile() throws IOException {
        try (var randomAccessFile = new RandomAccessFile("iotests\\test4.txt", "rw");
             var dis = new DataInputStream(new FileInputStream("iotests\\test5.txt"))) {
            randomAccessFile.writeChars("hello world");
            final var string = dis.readUTF();
            assertEquals("hello world", string);
        }
        assertTrue(Files.deleteIfExists(Path.of("iotests\\test4.txt")));
    }

    //q15 test 5
    //Remember that RandomAccessFile implements DataInput as well as DataOutput interfaces. Therefore,
    // in this case, you can use raf as an instance of DataOutput and call its writeUTF(String) method.
    @Test
    void testRandomAccessFileWriteMethodUtf() throws IOException {
        try (var raf = new RandomAccessFile("iotests\\test5.txt", "rwd");
             var dis = new DataInputStream(new FileInputStream("iotests\\test5.txt"))) {
            raf.writeUTF("hello world");
            String value = dis.readUTF();
            assertEquals("hello world", value);
        }
    }

    private static void copy(String records1, String records2) {
        try (InputStream is = new FileInputStream(records1);
             OutputStream os = new FileOutputStream(records2);) {  //1
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {  //3
                os.write(buffer, 0, bytesRead);
                System.out.println("Read and written bytes " + bytesRead);
            }
        } catch (IOException e) { //4
            e.printStackTrace();
        }
    }

    void testVarInTryWithResourcesAreEffectivelyFinal() {
        copy("c:\\temp\\test1.txt", "c:\\temp\\test2.txt");
    }

    @Test
    void testBufferedReaderLinesMethod() {
        String testString = "Hallo dit is een test\nNa de break\nThird line";
        final var inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        final var strings = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .filter(s -> !s.contains("Na"))
                .collect(Collectors.joining("\n"));

        final var expected = "Hallo dit is een test\nThird line";

        assertEquals(expected, strings);
    }

    @Test
    void testInputStreamReadAllBytes() {
        String testString = "Hallo dit is een test\nNa de break\nThird line";
        final var inputStream = new ByteArrayInputStream(testString.getBytes(StandardCharsets.UTF_8));

        final var actual = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8)
                .lines()
                .filter(s -> !s.contains("Na"))
                .collect(Collectors.joining("\n"));

        final var expected = "Hallo dit is een test\nThird line";

        assertEquals(expected, actual);
    }
}
