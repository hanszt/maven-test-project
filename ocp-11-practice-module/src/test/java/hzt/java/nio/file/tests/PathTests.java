package hzt.java.nio.file.tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.stream.IntStream;

import static hzt.OsAssumptions.assumeIsWindowsOs;
import static java.nio.file.Files.copy;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static org.junit.jupiter.api.Assertions.*;

class PathTests {

    //q2 test 4
    //NOTE: Implementation for relativize method has changed in Java 11. It normalizes the paths before relativizing.
    // That is why, for Java 11, it prints 6 3 1 1, while on Java 8, it prints 6 3 9 9.

    //You need to understand how relativize works for the purpose of the exam.
    // The basic idea of relativize is to determine a path, which,
    // when applied to the original path will give you the path that was passed.
    // For example, "a/b" relativize "c/d" is "../../c/d" because if you are in directory b,
    // you have to go two steps back and then one step forward to c and another step forward to d to be in d.
    // However, "a/c" relativize "a/b" is "../b"
    // because you have to go only one step back to a and then one step forward to b.
    @Test
    void testPathNormalizeAndRelativize() {
        assumeIsWindowsOs();
        Path path = Paths.get("photos\\..\\beaches\\.\\calangute\\a.txt");
        Path normalizedPath = path.normalize();
        Path oriRelativeToNorm = path.relativize(normalizedPath);
        Path normRelativeToOri = normalizedPath.relativize(path);

        System.out.println("normalizedPath = " + normalizedPath);
        System.out.println("oriRelativeToNorm = " + oriRelativeToNorm);
        System.out.println("normRelativeToOri = " + normRelativeToOri);

        assertEquals(6, path.getNameCount());
        assertEquals(3, normalizedPath.getNameCount());
        assertEquals(1, oriRelativeToNorm.getNameCount());
        assertEquals(1, normRelativeToOri.getNameCount());
    }

    //Q5 java io test I
    // f the argument is a relative path (i.e. if it doesn't start with a root),
    // the argument is simply appended to the path to produce the result.
    @Test
    void testPathRelativize() {
        assumeIsWindowsOs();

        Path p1 = Path.of("photos\\..\\beaches\\.\\calangute\\a.txt");
        Path p2 = Path.of("photos\\a.txt");
        Path p3 = Path.of("photos\\..\\beaches\\.\\calangute\\test\\b.txt");

        Path p1RelativeToP2 = p1.relativize(p2);
        Path p2RelativeToP1 = p2.relativize(p1);
        Path p1RelativeToSelf = p1.relativize(p1);
        Path p1RelativeToP3 = p1.relativize(p3);

        System.out.println("p1RelativeToP2 = " + p1RelativeToP2);
        System.out.println("p2RelativeToP1 = " + p2RelativeToP1);
        System.out.println("p1RelativeToSelf = " + p1RelativeToSelf);
        System.out.println("p1RelativeToP3 = " + p1RelativeToP3);

        assertAll(
                () -> assertEquals(5, p1RelativeToP2.getNameCount()),
                () -> assertEquals(5, p2RelativeToP1.getNameCount()),
                () -> assertEquals(1, p1RelativeToSelf.getNameCount()),
                () -> assertEquals(3, p1RelativeToP3.getNameCount())
        );
    }

    //    Remember the following points about Path.subpath(int beginIndex, int endIndex)
    //    1. Indexing starts from 0.
    //    2. Root (i.e. c:\) is not considered as the beginning.
    //    3. name at beginIndex is included but name at endIndex is not.
    //    4. paths do not start or end with \.
    //    Thus, if your path is "c:\\a\\b\\c",  subpath(1,1) will cause IllegalArgumentException to be thrown.
    //    subpath(1,2) will correspond to b. subpath(1,3) will correspond to b/c.
    //
    //    Remember the following 4 points about Path.getName() method :
    //    1. Indices for path names start from 0.
    //    2. Root (i.e. c:\) is not included in path names.
    //    3. \ is NOT a part of a path name.
    //    4. If you pass a negative index or a value greater than or equal to the number of elements,
    //    or this path has zero name elements, java.lang.IllegalArgumentException is thrown.
    //
    //    It DOES NOT return null.  Thus, for example,
    //
    //    If your Path is "c:\\code\\java\\PathTest.java",  p1.getRoot() is c:\
    //    ((For Unix based environments, the root is usually / ). p1.getName(0) is code p1.getName(1)
    //    is java p1.getName(2) is PathTest.java p1.getName(3) will cause IllegalArgumentException to be thrown.
    @Test
    void testSubPath() {
        assumeIsWindowsOs();
        Path p1 = Paths.get("c:\\a\\b\\c");
        String x = p1.getName(1).toString();
        String y = p1.subpath(1, 2).toString();
        assertEquals("b : b", x + " : " + y);
    }

    //q19 test 4
//    The following 4 points about Path.getName() method are good to know as well:
//    1. Indices for path names start from 0.
//    2. Root (i.e. c:\) is not included in path names.
//    3. \ is NOT a part of a path name.
//    4. If you pass a negative index or a value greater than or equal to the number of elements,
//    or this path has zero name elements, java.lang.IllegalArgumentException is thrown.
//
//    It DOES NOT return null.  Thus, for example, If your Path is "c:\\code\\java\\PathTest.java",  p1.getRoot()
//    is c:\ ((For Unix based environments, the root is usually / ). p1.getName(0) is
//    code p1.getName(1) is java p1.getName(2) is PathTest.java p1.getName(3) will cause IllegalArgumentException to be thrown.
    @Test
    void testPathGetRootYieldsRootWithSeparator() {
        assumeIsWindowsOs();
        Path p1 = Paths.get("c:\\main\\project\\Starter.java");
        String root = p1.getRoot().toString();
        final var expected = "c:" + File.separator;
        System.out.println("expected = " + expected);
        assertEquals(expected, root);
    }

    //Q48 test 5
//    If p2 is a symbolic link, then that link, and not the target of that link, will be replaced .
//
//
//    If p1 is a symbolic link, then the final target of the link is copied to p2.
    @Test
    void testSymbolicLink() throws IOException {
        assumeIsWindowsOs();
        Path target = createTextFile();
        assertThrows(PrivilegedActionException.class, () -> createSymbolicLinkAndCopy(target));
    }

    private Path createTextFile() throws IOException {

        Path filePath = Path.of("", "target_link.txt");
        Files.writeString(filePath, IntStream.range(0, 10000)
                .mapToObj(i -> i + System.lineSeparator())
                .reduce("", String::concat), CREATE, TRUNCATE_EXISTING);
        return filePath;
    }

    private Path createSymbolicLinkAndCopy(Path target) throws IOException, PrivilegedActionException {
        Path link = Path.of("", "symbolic_link.txt");
        if (Files.exists(link)) {
            Files.delete(link);
        }
        final var symbolicLink = AccessController
                .doPrivileged((PrivilegedExceptionAction<Path>) () -> getSymbolicLink(target, link));
        assertThrows(FileSystemException.class, () -> copy(target, symbolicLink, StandardCopyOption.REPLACE_EXISTING));
        return symbolicLink;
    }

    private Path getSymbolicLink(Path target, Path link) throws IOException {
        return Files.createSymbolicLink(link, target);
    }

    @ParameterizedTest
    @ValueSource(strings = {"src/test/java/hzt/EnumTests.java", "README.md"})
    void testImplicitConversionToPath_pathsExist(Path path) {
        assertTrue(path.toFile().exists());
    }
}
