package hzt.functional_patterns.loan_pattern;

import org.jooq.lambda.Unchecked;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileExporterTest {

    @Test
    void testFileExporter() {
        var fileExporter = new FileExporter();
        final var accountExportWriter = new AccountExportWriter();
        final var userExportWriter = new UserExportWriter();

        final var accountExportFile = fileExporter
                .exportFile("accountExport.csv", Unchecked.consumer(accountExportWriter::writeAccounts));
        final var userExportFile = fileExporter
                .exportFile("userExport.csv", Unchecked.consumer(userExportWriter::writeUsers));

        assertAll(
                () -> assertTrue(accountExportFile.isPresent()),
                () -> assertTrue(userExportFile.isPresent())
        );
        userExportFile
                .map(File::toPath)
                .ifPresent(Unchecked.consumer(Files::delete));
        accountExportFile
                .map(File::toPath)
                .ifPresent(Unchecked.consumer(Files::delete));
    }

}
