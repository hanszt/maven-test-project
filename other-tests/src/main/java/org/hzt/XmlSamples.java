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
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.function.Consumer;

public final class XmlSamples {

    private XmlSamples() {
    }

    /**
     * @param xmlString the xml subject pretty print
     * @return A pretty printed xml
     * @see <a href="https://www.baeldung.com/java-pretty-print-xml">Pretty-Print XML in Java</a>
     */
    public static String prettyPrintXml(String xmlString, Consumer<Transformer> transformerConfigurer) {
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
    private static String readPrettyPrintXslt() throws IOException {
        final var name = "/pretty-print.xsl";
        try (final var resourceAsStream = XmlSamples.class.getResourceAsStream(name)) {
            final var inputStream = Objects.requireNonNull(resourceAsStream, "Resource not found at " + name);
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
