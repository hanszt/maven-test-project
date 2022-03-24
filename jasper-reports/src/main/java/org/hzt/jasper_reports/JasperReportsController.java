package org.hzt.jasper_reports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hzt.jasper_reports.service.JasperReportsService;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class JasperReportsController implements Initializable {

    @FXML
    private VBox mainPanel;
    @FXML
    private Label welcomeText;

    private final Stage stage;
    private final FileChooser fileChooser = new FileChooser();
    private final ListView<File> fileListView = new ListView<>();

    public JasperReportsController(Stage stage) {
        this.stage = stage;
    }

    @FXML
    protected void onOpenFileChooserButtonClick() {
        welcomeText.setText("File chooser open");
        final var file = fileChooser.showOpenDialog(stage);
        fileListView.getItems().add(file);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        final var convertButton = new Button("Convert to jasper file");
        convertButton.setOnAction(this::convertSelectedFile);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        mainPanel.getChildren().addAll(convertButton, fileListView);
    }

    private void convertSelectedFile(ActionEvent e) {
        final var fileName = fileListView.getSelectionModel()
                .getSelectedItems()
                .stream()
                .findFirst()
                .map(File::getName)
                .orElse("/jrxml/employeeReport.jrxml");
        executeJasperReportLogic(fileName);
    }

    private static void executeJasperReportLogic(String fileName) {
        final var jasperReportsService = new JasperReportsService();
        jasperReportsService.compileJasperReport(fileName)
                .ifPresentOrElse(r -> jasperReportsService.save(r, "employeeReport.jasper"),
                        () -> jasperReportsService
                                .exportToPdf("/jrxml/bookAvailability.jrxml", "bookAvailabilityReport.pdf"));

    }
}
