package org.hzt;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.util.Optional;
import java.util.function.Consumer;

public final class XmlSamples {

    private XmlSamples() {
    }

    /**
     * @param xmlString the xml subject pretty print
     * @return A pretty printed xml
     * @see <a href="https://www.baeldung.com/java-pretty-print-xml">Pretty-Print XML in Java</a>
     */
    public static String prettyPrintByTransformer(String xmlString, Consumer<Transformer> transformerConfigurer) {
        try {
            final var src = new InputSource(new StringReader(xmlString));
            final var factory = DocumentBuilderFactory.newInstance();
            //noinspection HttpUrlsUsage
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            final Document document = factory.newDocumentBuilder().parse(src);

            final var transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");

            final var transformer = transformerFactory.newTransformer(new StreamSource(new StringReader(readPrettyPrintXslt())));
            transformerConfigurer.accept(transformer);

            final var stringWriter = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            return stringWriter.toString();
        } catch (TransformerException | ParserConfigurationException | IOException | SAXException e) {
            throw new IllegalStateException("Error occurs when pretty-printing xml:\n" + xmlString, e);
        }
    }

    /**
     * @return the content of the pretty-print.xsl makes sure the empty nodes are filtered out
     */
    private static String readPrettyPrintXslt() {
        final var path = Optional.ofNullable(XmlSamples.class.getResource("/pretty-print.xsl"))
                .map(URL::getFile)
                .map(File::new)
                .map(File::toPath)
                .orElseThrow();
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
