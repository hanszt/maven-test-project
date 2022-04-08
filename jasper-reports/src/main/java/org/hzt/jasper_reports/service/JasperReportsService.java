package org.hzt.jasper_reports.service;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;

public class JasperReportsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportsService.class);

    public Optional<JasperReport> compileJasperReport(String reportName) {
        try {
            InputStream employeeReportStream = getClass().getResourceAsStream(reportName);
            final var jasperReport = JasperCompileManager.compileReport(employeeReportStream);
            LOGGER.info("{}", jasperReport);
            return Optional.of(jasperReport);
        } catch (JRException e) {
            LOGGER.error("Could not load report at: {}", reportName, e);
            return Optional.empty();
        }
    }

    public void exportToPdf(File inputFile, String pdfFileName) {
        final var exporter = new JRPdfExporter();

        exporter.setExporterInput(new SimpleExporterInput(inputFile));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfFileName));

        final var reportConfig = new SimplePdfReportConfiguration();
        reportConfig.setSizePageToContent(true);
        reportConfig.setForceLineBreakPolicy(false);

        final var exportConfig = new SimplePdfExporterConfiguration();
        exportConfig.setMetadataAuthor("hzt");
        exportConfig.setEncrypted(true);
        exportConfig.setAllowedPermissionsHint("PRINTING");

        exporter.setConfiguration(reportConfig);
        exporter.setConfiguration(exportConfig);

        try {
            exporter.exportReport();
        } catch (JRException e) {
            LOGGER.error("Could not export report", e);
        }
    }

    public void save(JasperReport jasperReport, String name) {
        try {
            JRSaver.saveObject(jasperReport, name);
        } catch (JRException e) {
            LOGGER.error("Could not save report with name {}", name, e);
        }
    }
}
