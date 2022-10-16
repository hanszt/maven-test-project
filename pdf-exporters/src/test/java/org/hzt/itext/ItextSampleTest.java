package org.hzt.itext;

import com.itextpdf.text.DocumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hzt.itext.ItextSample.createHelloWorldPdf;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ItextSampleTest {

    @Test
    @DisplayName("Test create hello world pdf with itext")
    void testCreateHelloWorldPdfWithItext() throws DocumentException, IOException {
        final var name = "iTextHelloWorld.pdf";
        final var outputFilePath = Path.of(name);
        try {
            final var content = "Hello World";

            createHelloWorldPdf(name, content);

            final var contentType = Files.probeContentType(outputFilePath);

            assertEquals("application/pdf", contentType);
        } finally {
            Files.delete(outputFilePath);
        }
    }
}
