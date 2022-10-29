package org.hzt;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class MaryQubitApp extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaryQubitApp.class);

    static final int SCALE = 4;
    static final int SPRITE_SIZE = 32;
    static final int CELL_SIZE = SPRITE_SIZE * SCALE;
    static final int HORIZONTAL_CELLS = 10;
    static final int VERTICAL_CELLS = 6;
    static final int BOARD_WIDTH = HORIZONTAL_CELLS * CELL_SIZE;
    static final int BOARD_HEIGHT = VERTICAL_CELLS * CELL_SIZE;
    private static final StringProperty helpTextProperty = new SimpleStringProperty("Help text appears here");
    static MapObject[][] map = new MapObject[HORIZONTAL_CELLS][VERTICAL_CELLS];
    private MapObject.Rainbow rainbow;

    private MapObject.ChickenCoop chickenCoop;
    private MapObject.Nest nest;

    public MaryQubitApp() {
        super();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Mary Had a Little Qubit");
        final var strangeBridge = new StrangeBridge();

        final var barn = new MapObject.Barn(new Location(2, 3), strangeBridge);
        rainbow = new MapObject.Rainbow(new Location(5, 0), strangeBridge);
        chickenCoop = new MapObject.ChickenCoop(new Location(5, 4), strangeBridge);
        nest = new MapObject.Nest(new Location(3, 4), strangeBridge);
        final var root = new Group();
        final var scene = new Scene(root, BOARD_WIDTH, BOARD_HEIGHT, Color.WHITE);
        primaryStage.setScene(scene);

        populateBackground(root);

        final var styles = "/styles.css";

        Optional.ofNullable(MaryQubitApp.class.getResource(styles))
                .map(URL::toExternalForm)
                .ifPresentOrElse(scene.getStylesheets()::add, () -> LOGGER.error("Could not find {}", styles));

        final var children = root.getChildren();
        children.addAll(barn, rainbow, new MapObject.Church(new Location(6, 2), strangeBridge), chickenCoop, nest);

        final var fox = new MapObject.Fox(new Location(7, 4), strangeBridge);
        fox.setDirection(Direction.LEFT);
        fox.setScaleX(.5);
        fox.setScaleY(.5);

        children.add(fox);

        helpTextProperty.set("Use the arrows to navigate Mary");
        var mary = new SpriteView.Mary(new Location(0, 3), this);
        children.addAll(createCells(mary));
        strangeBridge.setOpacity(0.5);

        children.addAll(strangeBridge, createHelpNode(), mary);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, ke -> moveMary(mary, ke));

        primaryStage.show();
    }

    public static void setHelpText(final String t) {
        helpTextProperty.set(t);
    }

    private static Group createHelpNode() {
        //Pressing X, H or C will activate/deactivate the gates
        final var help1 = new Label();
        help1.textProperty().bind(helpTextProperty);
        help1.setStyle("-fx-background-color: white;-fx-font-size: 1.3em;");

        final var answer = new Group();
        answer.getChildren().addAll(help1);
        answer.setTranslateY((VERTICAL_CELLS - 1.) * CELL_SIZE);
        answer.setTranslateX((HORIZONTAL_CELLS - 3.) * CELL_SIZE);
        return answer;

    }

    private void populateBackground(final Group root) {
        // Image by Victor Szalvay: http://www.flickr.com/photos/55502991@N00/172603855
        final var image = "/images/field.jpg";
        final var background = Optional.ofNullable(getClass().getResource(image))
                .map(URL::toString)
                .map(ImageView::new)
                .orElseThrow(() -> new IllegalStateException("Could not find " + image));
        background.setFitHeight(BOARD_HEIGHT);
        root.getChildren().add(background);

    }

    private static List<Rectangle> createCells(final SpriteView.Mary mary) {
        final IntFunction<Stream<Rectangle>> toRectangleStream = x -> IntStream.range(0, VERTICAL_CELLS)
                .mapToObj(y -> createRectangle(mary, x, y));
        return IntStream.range(0, HORIZONTAL_CELLS)
                .mapToObj(toRectangleStream)
                .flatMap(s -> s)
                .toList();
    }

    private static Rectangle createRectangle(final SpriteView.Mary mary, final int x, final int y) {
        final var x1 = x * CELL_SIZE;
        final var y1 = y * CELL_SIZE;
        final var rect = new Rectangle(x1, y1, CELL_SIZE, CELL_SIZE);
        rect.setFill(Color.rgb(0, 0, 0, 0));
        rect.setStrokeType(StrokeType.INSIDE);
        rect.setStroke(Color.BLACK);
        rect.setOnMousePressed(e -> mary.moveTo(new Location(x, y)));
        return rect;
    }

    private void moveMary(final SpriteView.Shepherd mary, final KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case W, UP -> mary.move(Direction.UP);
            case A, LEFT -> mary.move(Direction.LEFT);
            case S, DOWN -> mary.move(Direction.DOWN);
            case D, RIGHT -> mary.move(Direction.RIGHT);
            case X -> chickenCoop.toggleActive();
            case H -> nest.toggleActive();
            case C -> rainbow.toggleActive();
            case ESCAPE -> Platform.exit();
            default -> {
            }
        }
    }

    public static void main(final String[] args) {
        launch(args);
    }

    public enum Direction {
        DOWN(0), LEFT(1), RIGHT(2), UP(3);
        private final int offset;

        Direction(final int offset) {
            this.offset = offset;
        }

        public int getOffset() {
            return offset;
        }

        public int getXOffset() {
            return switch (this) {
                case LEFT -> -1;
                case RIGHT -> 1;
                default -> 0;
            };
        }

        public int getYOffset() {
            return switch (this) {
                case UP -> -1;
                case DOWN -> 1;
                default -> 0;
            };
        }
    }

    public record Location(int cellX, int cellY) {

        public Location offset(final int x, final int y) {
            return new Location(cellX + x, cellY + y);
        }

        public Direction directionTo(final Location loc) {
            if (Math.abs(loc.cellX - cellX) > Math.abs(loc.cellY - cellY)) {
                return (loc.cellX > cellX) ? Direction.RIGHT : Direction.LEFT;
            } else {
                return (loc.cellY > cellY) ? Direction.DOWN : Direction.UP;
            }
        }
    }
}
