package hzt.only_jdk;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilesTests {


    @Test
    void testStreamFromPath() {
        final var path = Path.of("/src/main/resources/test");

        final var paths = Stream.of(path)
                .mapMulti(Path::forEach)
                .map(Path::toString)
                .toList();

        System.out.println(paths);

        assertEquals(List.of("src", "main", "resources", "test"), paths);
    }

}
