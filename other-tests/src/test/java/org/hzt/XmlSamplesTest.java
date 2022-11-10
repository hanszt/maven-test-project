package org.hzt;

import org.junit.jupiter.api.Test;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlSamplesTest {

    @Test
    void testPrettyPrintXml() {
        String inputXml = """
                <emails> <email> <from>Kai</from> <to>Amanda</to> <time>2018-03-05</time>
                <subject>I am flying to you</subject></email> <email>
                <from>Jerry</from> <to>Tom</to> <time>1992-08-08</time> <subject>Hey Tom, catch me if you can!</subject>
                </email> </emails>
                """;

        final var prettyPrintXml = XmlSamples.prettyPrintXml(inputXml, this::configureTransformer);
        System.out.println(prettyPrintXml);

        assertEquals(14, prettyPrintXml.lines().count());
    }

    private void configureTransformer(Transformer transformer) {
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        //noinspection HttpUrlsUsage
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(2));
    }

}
