package hzt.functional_patterns.loan_pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;
import java.util.function.Consumer;

public class FileExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileExporter.class);

    public Optional<File> exportFile(String fileName, Consumer<Writer> contentWriter) {
        File file = new File(fileName);
        try (Writer writer = new FileWriter(file)) {
            contentWriter.accept(writer);
            return Optional.of(file);
        } catch (IOException e) {
            LOGGER.error("A Io error occurred. An Email should be send to admin", e);
            return Optional.empty();
        }
    }
}
