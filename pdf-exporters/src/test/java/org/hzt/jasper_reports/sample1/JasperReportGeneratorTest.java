package org.hzt.jasper_reports.sample1;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRSaver;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JasperReportGeneratorTest {

    @Test
    void generateJasperReport() throws JRException, IOException {
        final var destFileName = "ned.pdf";
        final var path = Path.of(destFileName);
        try {
            JasperReportGenerator.generateCountryPdf(destFileName, getDataSource());
            final var contentType = Files.probeContentType(path);

            assertEquals("application/pdf", contentType);
        } finally {
            Files.delete(path);
        }
    }

    private static JRDataSource getDataSource() {
        final var url = ofNullable(JasperReportGeneratorTest.class.getResource("/nederland.png"))
                .map(URL::getFile)
                .orElseThrow();

        return new JRBeanCollectionDataSource(List.of(new Country("Nl", "Netherlands", url)
        ));
    }

    @Test
    void testCompileReport() throws JRException, IOException {
        final var template = ofNullable(JasperReportGenerator.class.getResource("/jrxml/report.jrxml"))
                .map(URL::getFile)
                .map(File::new)
                .orElseThrow();

        final var jasperFileName = "countryReport.jasper";
        try {

            final var jasperReport = JasperReportGenerator.compileToJasperReport(template);
            JRSaver.saveObject(jasperReport, jasperFileName);

            final var contentType = Objects.requireNonNullElse(Files.probeContentType(template.toPath()), "Not determined");

            assertEquals("Not determined", contentType);
        } finally {
            Files.delete(Path.of(jasperFileName));
        }

    }

}
