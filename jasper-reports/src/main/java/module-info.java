module com.example.jasperreports {
    requires javafx.controls;
    requires javafx.fxml;
    requires jasperreports;
    requires slf4j.api;

    opens org.hzt.jasper_reports to javafx.fxml;
    exports org.hzt.jasper_reports;
    exports org.hzt.jasper_reports.service;
}
