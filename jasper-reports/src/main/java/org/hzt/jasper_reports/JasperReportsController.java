package org.hzt.jasper_reports;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hzt.jasper_reports.service.JasperReportsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class JasperReportsController implements Initializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JasperReportsController.class);

    @FXML
    private Button convertToJasperFileButton;
    @FXML
    private Button convertToPdfButton;
    @FXML
    private VBox mainPanel;

    private final FileChooser fileChooser = new FileChooser();
    private final ListView<File> fileListView = new ListView<>();

    private final Stage stage;
    private final JasperReportsService jasperReportService;

    public JasperReportsController(Stage stage, JasperReportsService jasperReportService) {
        this.stage = stage;
        this.jasperReportService = jasperReportService;
    }

    @FXML
    protected void onOpenFileChooserButtonClick() {
        final var file = fileChooser.showOpenDialog(stage);
        fileListView.getItems().add(file);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        convertToPdfButton.setOnAction(this::convertSelectedFileToPdf);
        convertToJasperFileButton.setOnAction(this::saveSelectedToJasperObject);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fileListView.getItems().addAll(getJasperReportResourceFiles());
        fileListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        mainPanel.getChildren().addAll(fileListView);
    }

    private List<File> getJasperReportResourceFiles() {
        return Optional.ofNullable(getClass().getResource("/jrxml"))
                .map(URL::getFile)
                .map(File::new)
                .filter(File::isDirectory)
                .map(File::listFiles)
                .map(Arrays::asList)
                .orElse(List.of());
    }

    private void convertSelectedFileToPdf(ActionEvent e) {
        getFirstSelectedFileAndExecute()
                .ifPresentOrElse(this::convertToPdf, () -> LOGGER.error("No item selected"));
    }

    private void saveSelectedToJasperObject(ActionEvent e) {
        getFirstSelectedFileAndExecute()
                .map(File::getName)
                .ifPresentOrElse(this::saveReportObject, () -> LOGGER.error("No item selected"));
    }

    private Optional<File> getFirstSelectedFileAndExecute() {
        return fileListView.getSelectionModel()
                .getSelectedItems()
                .stream()
                .findFirst();
    }


    private void saveReportObject(String fileName) {
        LOGGER.info("Saving {} to jasper object", fileName);
        jasperReportService.compileJasperReport(fileName)
                .ifPresent(r -> jasperReportService.save(r, fileName.replace(".jrxml", ".jasper")));
    }

    private void convertToPdf(File file) {
        LOGGER.info("Converting {} to pdf", file);
        jasperReportService
                .exportToPdf(file, file.getName().replace(".jrxml", ".pdf"));


    }
}
