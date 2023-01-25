package hzt.only_jdk;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SerializationTests {

    /**
     * @see <a href="https://youtu.be/V_mzFdFTk6I?t=900">class vs record reading by serialization</a>
     * <p>
     * The canonical constructor of a class is not called when deserializing a file to an object of that class
     * <p>
     * The canonical constructor of a record is called when deserializing a file to an object of that record
     * <p>
     * writeReplace, readResolve serializes and deserializes class via records to close the security breach
     */
    @Nested
    class DeserializationOfRecordVsClassTest {

        private static boolean validating = false;

        @Test
        @DisplayName("during deserialization to a class with validation in constructor, the constructor and validation logic is not called (security hazard)")
        void testClass() {
            validating = false;
            var rangeClass = new RangeClass(10, 0);
            System.out.println("corrupted rangeClass = " + rangeClass);
            final var targetFileName = "range-class-10-0.dat";
            assertDoesNotThrow(() -> writeAndReadBySerialization(rangeClass, targetFileName, () -> validating = true));
        }

        @Test
        @DisplayName("during deserialization to a record with validation in constructor, the constructor and validation logic should be called")
        void testRecord() {
            validating = false;
            var rangeClass = new RangeRecord(10, 0);
            System.out.println("corrupted rangeRecord = " + rangeClass);
            final var targetFileName = "range-record-10-0.dat";
            assertThrows(IllegalArgumentException.class,
                    () -> writeAndReadBySerialization(rangeClass, targetFileName, () -> validating = true));
        }

        private static <T> T writeAndReadBySerialization(T rangeClass, String targetFileName, Runnable runnable) {
            final var path = Path.of(targetFileName);
            try (var objectOutputStream = new ObjectOutputStream(Files.newOutputStream(path));
                 var objectInputStream = new ObjectInputStream(Files.newInputStream(path))) {
                objectOutputStream.writeObject(rangeClass);
                runnable.run();
                final var o = objectInputStream.readObject();
                //noinspection unchecked
                return (T) o;
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalArgumentException(e);
            } finally {
                deleteSerializedFile(path);
            }
        }

        private static void deleteSerializedFile(Path path) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }


        record RangeRecord(int begin, int end) implements Serializable {

            RangeRecord {
                System.out.println("RangeRecord constructor call");
                if (validating && begin > end) {
                    final var message = "begin should be smaller than end";
                    System.out.println("RangeRecord: " + message);
                    throw new IllegalArgumentException(message);
                }
            }
        }

        @SuppressWarnings("ClassCanBeRecord")
        private static final class RangeClass implements Serializable {

            private final int begin;
            private final int end;

            public RangeClass(int begin, int end) {
                System.out.println("RangeClass constructor call");
                if (validating && begin > end) {
                    final var message = "begin should be smaller than end";
                    System.out.println("RangeClass: " + message);
                    throw new IllegalArgumentException(message);
                }
                this.begin = begin;
                this.end = end;
            }

            @Override
            public String toString() {
                return "RangeClass{" +
                        "begin=" + begin +
                        ", end=" + end +
                        '}';
            }
        }
    }
}
