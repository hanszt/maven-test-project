package hzt.only_jdk;

import io.vavr.Lazy;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hzt.utils.It.println;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class HashingAlgorithmsTest {

    private static final int STANDARD_HEXADECIMAL_HASH_LENGTH = 64;
    public static final Lazy<String> readWordsInWorksOfShakespeareLazy = Lazy.of(HashingAlgorithmsTest::readWordsInWorksOfShakespeare);

    @Test
    void testSha3_256Hashing() {
        final var wordsOfShakespeare = readWordsInWorksOfShakespeareLazy.get();

        final var hash = HashingAlgorithms.toHexadecimalHash(wordsOfShakespeare, HashingAlgorithms.Algorithm.SHA3_256);

        assertAll(
                () -> assertEquals(STANDARD_HEXADECIMAL_HASH_LENGTH, hash.length()),
                () -> assertEquals("62778946b8317462dd5c4100a4068f5e57351fe20da0006f1be02ce08683aaee", hash)
        );
    }

    @ParameterizedTest
    @MethodSource("hashTestArguments")
    void testSha256Hashing(String input, String expected) {
        final var hash = HashingAlgorithms.toHexadecimalHash(input, HashingAlgorithms.Algorithm.SHA_256);

        assertAll(
                () -> assertEquals(STANDARD_HEXADECIMAL_HASH_LENGTH, hash.length()),
                () -> assertEquals(expected, hash)
        );
    }

    @TestFactory
    Stream<DynamicTest> testSha3_256HashingByTestFactory() {
        return hashTestArguments()
                .map(this::dynamicTestSha256Hashing);
    }

    DynamicTest dynamicTestSha256Hashing(Arguments arguments) {
        final var args = arguments.get();
        String input = (String) args[0];
        String expected= (String) args[1];
        final var hash = HashingAlgorithms.toHexadecimalHash(input, HashingAlgorithms.Algorithm.SHA_256);

        final var name = "The sha 3 256 hash of " + abbreviate(input, 30) + " should be " + expected;
        return dynamicTest(name, () -> assertAll(
                () -> assertEquals(STANDARD_HEXADECIMAL_HASH_LENGTH, hash.length()),
                () -> assertEquals(expected, hash)
        ));
    }

    private static String abbreviate(String string, int maxLength) {
        return string.codePoints()
                .limit(maxLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


    private static Stream<Arguments> hashTestArguments() {
        return Stream.of(
                arguments("Hallo", "753692ec36adb4c794c973945eb2a99c1649703ea6f76bf259abb4fb838e013e"),
                arguments("Dit is een test", "2c2d00b7adc331709202290b7626d42759447800306307f452ce150e42cfb762"),
                arguments(readWordsInWorksOfShakespeareLazy.get(), "45a6c794bb9d9700d7bf68b9e1ecb167e42bdb981abef3a6e12ae7cf08b2ef07")
        );
    }

    @NotNull
    private static String readWordsInWorksOfShakespeare() {
        final var userDir = Path.of(System.getProperty("user.dir")).getParent();
        final var path = userDir.resolve("_input").resolve("shakespeareworks.txt");
        println("Reading " + path + "...");
        try (final var lines = Files.lines(path)) {
            return lines.collect(Collectors.joining());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
