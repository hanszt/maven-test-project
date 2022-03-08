package hzt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilePermission;
import java.io.FileReader;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IoSamples {

    //    The caller of this method passes in a Function that takes a java.io.File
    //    object and performs whatever operation is need to be done on that file.
    //    It is required that the above code must ensure that the caller only reads a file
    //    and is not able to overwrite or delete it irrespective of what level of permission the caller has.
    //    How can this be done?

    // Source: Enthuware Java11OCP Test 1 Q 6
    //Code is refactored
    public <R> List<R> executePrivilegedFunction(List<File> files, Function<File, R> fileMapper) {
        return files.stream()
                .map(file -> convertFileIfReadable(file, fileMapper))
                .collect(Collectors.toUnmodifiableList());
    }

    private static <R> R convertFileIfReadable(File file, Function<File, R> fileMapper) {
        var permission = new FilePermission(file.getPath(), "read");
        permission.newPermissionCollection().add(permission);
        return AccessController.doPrivileged((PrivilegedAction<R>) () -> fileMapper.apply(file));
    }

    // Source: Enthuware Java11OCP Test 1 Q 6

    //    A Reader object just gives you a readable stream.
    //    Normally, you cannot go back in a stream to read the data that has already been read.
    //    However, some readers do allow this facility by maintaining the data in a buffer.
    //    The markSupported, mark, and reset methods help you go back and forth in the data stream
    //    if the underlying reader supports it. They allow you to set a point in the stream by calling
    //    the mark method. This point is like a bookmark in a book. You can return back to the same point
    //    by calling the reset method. Any call to read after reset will return the data right after
    //    the bookmark.  BufferedReader does provide this facility, therefore r.markSupported() returns true.
    //    Now, the mark method sets the bookmark in the stream right after A. The parameter 100 is the limit
    //    on the number of characters that may be read while still preserving the mark. The two readLines
    //
    //    calls after calling mark will return B and C. The call to reset will move the reader back to point
    //    right after A and therefore, the subsequent call to readline will return B.
    //    The second call to reset will again move the reader back to the point after A.
    //    The final call to readLine will thus return B.
    public static List<String> bufferedReaderWithMarker(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            if (reader.markSupported()) {
                List<String> strings = new ArrayList<>();
                strings.add(reader.readLine());
                reader.mark(100);
                strings.add(reader.readLine());
                strings.add(reader.readLine());
                reader.reset();
                strings.add(reader.readLine());
                reader.reset();
                strings.add(reader.readLine());
                return strings;
            } else {
                throw new IllegalStateException("Mark Not Supported");
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
