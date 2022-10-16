package org.hzt;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public final class PdfContentReader {

    private PdfContentReader() {
    }

    /**
     * @param file The pdf file to read text from
     * @return the file content
     * @see <a href="https://stackoverflow.com/questions/4784825/how-to-read-pdf-files-using-java">How to read PDF files using Java</a>
     */
    public static String readPdfContent(File file) {
        try (PDDocument document = PDDocument.load(file)) {
            if (!document.isEncrypted()) {
                PDFTextStripper stripper = new PDFTextStripper();
                return stripper.getText(document);
            }
            throw new IllegalStateException("'" + file + "' is encrypted");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
