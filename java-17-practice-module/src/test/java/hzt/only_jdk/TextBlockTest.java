package hzt.only_jdk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextBlockTest {

    public static final String XML = """
            <?xml version="1.0" encoding="UTF-8"?>
            <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                <modelVersion>4.0.0</modelVersion>
                            
                <groupId>org.example</groupId>
                <artifactId>Java15Test</artifactId>
                <version>1.0-SNAPSHOT</version>
                <build>
                    <plugins>
                        <plugin>
                            <groupId>org.apache.maven.plugins</groupId>
                            <artifactId>maven-compiler-plugin</artifactId>
                            <configuration>
                                <source>%d</source>
                                <target>%d</target>
                                <compilerArgs>--enable-preview</compilerArgs>
                            </configuration>
                        </plugin>
                    </plugins>
                </build>
                         
            </project>
            """;

    @Test
    void testBlock() {
        String javaVersion = "17";
        String xml = XML.replace("%d", javaVersion);

        assertAll(
                () -> assertTrue(xml.contains(javaVersion)),
                () -> assertEquals(24L, XML.lines().count())
        );
    }

}
