module com.example.pdf.exporters {

    requires jasperreports;
    requires slf4j.api;
    requires itextpdf;
    requires java.sql;

    opens org.hzt.itext;
    opens org.hzt.jasper_reports.sample1;

    exports org.hzt.jasper_reports.sample1;
}
