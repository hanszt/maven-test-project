package org.hzt;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.function.DoubleUnaryOperator;

public class TimeComplexityPlotter extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeComplexityPlotter.class);

    static final DoubleUnaryOperator classic = x -> Math.exp(Math.pow(64. / 9. * x * Math.log(x) * Math.log(x), 1. / 3));
    static final DoubleUnaryOperator shor = x -> Math.pow(x, 3.);

    @Override
    public void start(Stage stage) {
        LOGGER.info("Starting graph to show time difference for factorization between the classical algorithm and Shor's algorithm...");

        final var functions = List.of(classic, shor);
        final var chart = plotFunctions(functions, 0.000001, 20);
        final var scene = new Scene(chart, 640, 480);

        final var style = "/style.css";
        Optional.ofNullable(getClass().getResource(style))
                .map(URL::toExternalForm)
                .ifPresentOrElse(scene.getStylesheets()::add, () -> LOGGER.error("Could not load {}", style));

        stage.setScene(scene);
        stage.show();
    }

    public static Chart plotFunctions(List<DoubleUnaryOperator> functions, double xStart, double xEnd) {
        final int div = 500;
        final double step = 1. / div * (xEnd - xStart);
        final Axis<Number> xAxis = new NumberAxis(xStart, xEnd, .1 * (xEnd - xStart));
        final Axis<Number> yAxis = new NumberAxis();
        final ObservableList<XYChart.Series<Number, Number>> series = FXCollections.observableArrayList();
        final var chart = new LineChart<>(xAxis, yAxis, series);
        chart.setCreateSymbols(false);
        int i = 0;
        for (DoubleUnaryOperator function : functions) {
            final XYChart.Series<Number, Number> mainSeries = new XYChart.Series<>();
            series.add(mainSeries);
            ObservableList<XYChart.Data<Number, Number>> data = FXCollections.observableArrayList();
            mainSeries.setData(data);
            for (double x = xStart; x < xEnd; x = x + step) {
                final double y = function.applyAsDouble(x);
                data.add(new XYChart.Data<>(x, y));
            }
            mainSeries.setName(i == 0 ? "classic" : "shor");
            i++;
        }
        xAxis.setLabel("number of bits");
        yAxis.setLabel("time required to factor");
        chart.setTitle("Time Complexity");
        return chart;
    }
}
