module com.example.pdf.exporters {

    requires jasperreports;
    requires org.slf4j;
    requires itextpdf;
    requires java.sql;
    requires org.apache.pdfbox;

    opens org.hzt.itext;
    opens org.hzt.jasper_reports.sample1;

    exports org.hzt.jasper_reports.sample1;
}
