package org.hzt.itext;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public final class ItextSample {

    private ItextSample() {
    }

    /**
     * @param name the name of the file
     * @param content the content to print to the pdf
     * @see <a href="https://www.baeldung.com/java-pdf-creation">Creating PDF Files in Java</a>
     */
    static void createHelloWorldPdf(String name, String content) throws DocumentException, FileNotFoundException {
        final var document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(name));

        document.open();
        final var font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        final var chunk = new Chunk(content, font);

        document.add(chunk);
        document.addCreationDate();
        document.close();
    }
}
