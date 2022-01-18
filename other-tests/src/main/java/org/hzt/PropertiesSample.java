package org.hzt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import static java.lang.System.out;

public class PropertiesSample {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesSample.class);
    private static final String PROP_FILE1_LOCATION = "./resources/testProject.properties";
    private static final String PROP_FILE2_LOCATION = "./resources/xml-properties.xml";
    private static final String PROP_NAME_QUEUE_MANAGER = "queue.manager";

    public static void main(String[] args) {
        LOGGER.info("Hello Test project");
        File propertiesFile = new File(PROP_FILE1_LOCATION);
        File propertiesXmlFile = new File(PROP_FILE2_LOCATION);

        try (final var inputStream = new BufferedInputStream(new FileInputStream(propertiesFile));
             final var fileOutputStreamXml = new FileOutputStream("./resources/newApp.xml");
             final var fileWriter = new FileWriter("./resources/newApp.properties")) {
            Properties properties1 = new Properties();
            properties1.load(inputStream);
            LOGGER.info(properties1.getProperty(PROP_NAME_QUEUE_MANAGER));
            LOGGER.info(properties1.getProperty("lariekoek"));

            Properties properties2 = new Properties();
            InputStream is2 = new BufferedInputStream(new FileInputStream(propertiesXmlFile));
            properties2.loadFromXML(is2);
            LOGGER.info(properties2.getProperty("fileIcon"));
            LOGGER.info(properties2.getProperty("lariekoek"));
            properties2.setProperty("fileIcon", "icon3.jpg");
            properties2.setProperty("appIcon", "appIcon.jpg");
            properties2.storeToXML(fileOutputStreamXml, "store to properties file");
            properties2.remove("imageIcon");
            properties2.store(fileWriter, "store to properties file");
            properties2.list(out);
            enumerate(properties1);
        } catch (IOException e) {
            LOGGER.error("", e);
        }

    }

    private static void enumerate(Properties appProps) {
        LambdaLogging.logIfInfoEnabled(() -> String.format("%nEnumeration of %s%n", appProps.toString()));
        Enumeration<Object> valueEnumeration = appProps.elements();
        while (valueEnumeration.hasMoreElements()) {
            LOGGER.info("Next element: {}", valueEnumeration.nextElement());
        }

        Enumeration<Object> keyEnumeration = appProps.keys();
        while (keyEnumeration.hasMoreElements()) {
            LOGGER.info("Next element: {}", keyEnumeration.nextElement());
        }

        int size = appProps.size();
        LOGGER.info("Size: {}", size);
    }
}
