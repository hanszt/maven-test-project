package org.hzt.jasper_reports;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hzt.jasper_reports.service.JasperReportsService;

import java.io.IOException;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("main-panel.fxml"));
        fxmlLoader.setControllerFactory(c -> new JasperReportsController(stage, new JasperReportsService()));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Jasper reports sample");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
