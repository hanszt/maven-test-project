package org.hzt.jasper_reports.sample1;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Source: <a href="https://github.com/hmkcode/Java/tree/master/java-jasper">java-jasper</a>
 */
public class JasperReportGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportGenerator.class);

    public static void main(String[] args) {
        try {
            final var fileName = "country-flags.pdf";
            LOGGER.info("generating jasper report '{}'...", fileName);
            final var dataSource = getDataSource();
            generateCountryPdf(fileName, dataSource);
            LOGGER.info("Report '{}' generated", fileName);
        } catch (JRException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void generateCountryPdf(String destFileName, JRDataSource dataSource) throws JRException {
        File template = Optional.ofNullable(JasperReportGenerator.class.getResource("/jrxml/report.jrxml"))
                .map(URL::getFile)
                .map(File::new)
                .orElseThrow();

        JasperReport jasperReport = compileToJasperReport(template);

        Map<String, Object> parameters = getParameters();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
    }

    static JasperReport compileToJasperReport(File template) throws JRException {
        return JasperCompileManager.compileReport(template.getAbsolutePath());
    }

    private static Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "hmkcode");
        return parameters;
    }

    private static JRDataSource getDataSource() {
        List<Country> countries = List.of(
                new Country("IS", "Iceland", "https://i.pinimg.com/originals/72/b4/49/72b44927f220151547493e528a332173.png"),
                new Country("TR", "Turkey", "https://i.pinimg.com/originals/82/63/23/826323bba32e6e5a5996062c3a3c662f.png"),
                new Country("ZA", "South Africa", "https://i.pinimg.com/originals/f5/c7/8d/f5c78da001b46e26481c04fb93473454.png"),
                new Country("PL", "Poland", "https://i.pinimg.com/originals/7f/ae/21/7fae21c4854010b11127218ead743863.png"),
                new Country("CA", "Canada", "https://i.pinimg.com/originals/4d/d4/01/4dd401733ba25e6442fc8696e04e5846.png"),
                new Country("PA", "Panama", "https://i.pinimg.com/originals/84/dc/e4/84dce49e52ebfb5b3814393069807e0a.png"),
                new Country("HR", "Croatia", "https://i.pinimg.com/originals/f5/8c/94/f58c94a2a2b3221328fc1e2b7acfa656.png"),
                new Country("JP", "Japan", "https://i.pinimg.com/originals/a5/d6/88/a5d688289cd6850016f14fe93b17da01.png"),
                new Country("DE", "Germany", "https://i.pinimg.com/originals/af/c9/b2/afc9b2592a9f1cf591e8a52256ae1e9f.png"),
                new Country("BR", "Brazil", "https://i.pinimg.com/originals/e4/03/c4/e403c4447a3bd8940459ae4f50856bed.png")
        );
        return new JRBeanCollectionDataSource(countries);
    }
}
