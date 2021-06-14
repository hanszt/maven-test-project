package com.dnb;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

import static java.lang.System.*;

public class PropertiesSample {

    private static final String PROP_FILE1_LOCATION = "./resources/testProject.properties";
    private static final String PROP_FILE2_LOCATION = "./resources/xml-properties.xml";
    private static final String PROP_NAME_QUEUE_MANAGER = "queue.manager";

    public static void main(String[] args) {
        out.println("Hello Test project");
        File propertiesFile = new File(PROP_FILE1_LOCATION);
        File propertiesXmlFile = new File(PROP_FILE2_LOCATION);

        try {
            Properties properties1 = new Properties();
            InputStream is1 = new BufferedInputStream(new FileInputStream(propertiesFile));
            properties1.load(is1);
            out.println(properties1.getProperty(PROP_NAME_QUEUE_MANAGER));
            out.println(properties1.getProperty("lariekoek"));

            Properties properties2 = new Properties();
            InputStream is2 = new BufferedInputStream(new FileInputStream(propertiesXmlFile));
            properties2.loadFromXML(is2);
            out.println(properties2.getProperty("fileIcon"));
            out.println(properties2.getProperty("lariekoek"));
            properties2.setProperty("fileIcon", "icon3.jpg");
            properties2.setProperty("appIcon", "appIcon.jpg");
            properties2.storeToXML(new FileOutputStream("./resources/newApp.xml"), "store to properties file");
            properties2.remove("imageIcon");
            properties2.store(new FileWriter("./resources/newApp.properties"), "store to properties file");
            properties2.list(out);
            enumerate(properties1);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void enumerate(Properties appProps) {
        out.printf("%nEnumeration of %s%n", appProps.toString());
        Enumeration<Object> valueEnumeration = appProps.elements();
        while (valueEnumeration.hasMoreElements()) {
            out.println(valueEnumeration.nextElement());
        }

        Enumeration<Object> keyEnumeration = appProps.keys();
        while (keyEnumeration.hasMoreElements()) {
            out.println(keyEnumeration.nextElement());
        }

        int size = appProps.size();
        out.println(size);
    }
}
