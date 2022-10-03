/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2019, Johan Vos and Stephen Chin
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of the copyright holder nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hzt;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.Optional;

public class SpriteView extends StackPane {
    final ImageView imageView;

    private SpriteView following;
    IntegerProperty number = new SimpleIntegerProperty(0);

    ObjectProperty<MaryQubitApp.Direction> direction = new SimpleObjectProperty<>();
    ObjectProperty<MaryQubitApp.Location> location = new SimpleObjectProperty<>();
    IntegerProperty frame = new SimpleIntegerProperty(1);
    int spriteWidth;
    int spriteHeight;
    Timeline walking;
    SpriteView follower;

    public SpriteView(Image spriteSheet, MaryQubitApp.Location loc) {
        imageView = new ImageView(spriteSheet);
        this.location.set(loc);
        setTranslateX(loc.getX() * MaryQubitApp.CELL_SIZE);
        setTranslateY(loc.getY() * MaryQubitApp.CELL_SIZE);
        ChangeListener<Object> updateImage = (ov, o, o2) -> imageView.setViewport(
                new Rectangle2D(frame.get() * spriteWidth,
                        direction.get().getOffset() * spriteHeight,
                        spriteWidth, spriteHeight));
        direction.addListener(updateImage);
        frame.addListener(updateImage);
        spriteWidth = (int) (spriteSheet.getWidth() / 3);
        spriteHeight = (int) (spriteSheet.getHeight() / 4);
        direction.set(MaryQubitApp.Direction.RIGHT);
        getChildren().add(imageView);
    }

    public SpriteView(Image spriteSheet, SpriteView following) {
        this(spriteSheet, following.getLocation().offset(-following.getDirection().getXOffset(), -following.getDirection().getYOffset()));
        number.set(following.number.get() + 1);
        this.following = following;
        setDirection(following.getDirection());
        following.follower = this;
        setMouseTransparent(true);
    }

    public final void setDirection(MaryQubitApp.Direction direction) {
        this.direction.setValue(direction);
    }

    public static class Mary extends Shepherd {
        // Image by Terra-chan: http://www.rpgmakervx.net/index.php?showtopic=29404
        static final Image MARY = loadImage("/images/mary.png");
        final MaryQubitApp parent;

        public Mary(MaryQubitApp.Location loc, MaryQubitApp maryQubitApp) {
            super(MARY, loc);
            this.parent = maryQubitApp;
        }
    }

    public static class Shepherd extends SpriteView {
        private final ObservableList<SpriteView> animals;

        public ObservableList<SpriteView> getAnimals() {
            return animals;
        }

        public Shepherd(Image spriteSheet, MaryQubitApp.Location loc) {
            super(spriteSheet, loc);
            animals = FXCollections.observableArrayList();
            animals.addListener(this::updateAnimals);

        }

        private void updateAnimals(ListChangeListener.Change<? extends SpriteView> change) {
            final ObservableList<Node> children = ((Group) getParent()).getChildren();
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved() || change.wasReplaced()) {
                    children.removeAll(change.getRemoved());
                    children.addAll(change.getAddedSubList());
                    SpriteView prev = this;
                    int number = -1;
                    for (SpriteView sprite : animals) {
                        sprite.following = prev;
                        sprite.number.set(++number);
                        prev.follower = sprite;
                        prev = sprite;
                    }
                }
            }
        }

        public void move(MaryQubitApp.Direction direction) {
            if (walking != null && walking.getStatus() == Animation.Status.RUNNING) {
                return;
            }
            int lx = location.getValue().getX();
            int ly = location.getValue().getY();
            int dx = direction.getXOffset();
            int dy = direction.getYOffset();
            if (animals.isEmpty()) {
                MaryQubitApp.setHelpText("Visit the barn to get a qubitlamb");
            }
            if ((dx < 0 && lx < 1) || (dy < 0 && ly < 1)) {
                return;
            }
            if ((dx > 0 && lx > MaryQubitApp.HORIZONTAL_CELLS - 2) || (dy > 0 && ly > MaryQubitApp.VERTICAL_CELLS - 2)) {
                return;
            }
            moveTo(location.getValue().offset(direction.getXOffset(), direction.getYOffset()));
            for (SpriteView animal : animals) {
                animal.moveTo(location.get());
            }
        }
    }

    public static class Lamb extends NumberedSpriteView {
        // Image by Mack: http://www.rpgmakervx.net/index.php?showtopic=15704
        static final Image LAMB = loadImage("/images/lamb.png");

        private final DoubleProperty valueProperty = new SimpleDoubleProperty(0);

        public Lamb(SpriteView following) {
            super(LAMB, following);
            direction.addListener((ov, o1, o2) -> changeDirection(o2));
            startAnimation();
            valueProperty.addListener(o -> imageView.setEffect(valueProperty.get() > .5 ? new InnerShadow(150, Color.BLACK) : null));
        }

        private void changeDirection(MaryQubitApp.Direction o2) {
            switch (o2) {
                case RIGHT -> {
                    label.setTranslateX(-4 * MaryQubitApp.SCALE);
                    label.setTranslateY(2 * MaryQubitApp.SCALE);
                }
                case LEFT -> {
                    label.setTranslateX(4 * MaryQubitApp.SCALE);
                    label.setTranslateY(2 * MaryQubitApp.SCALE);
                }
                case UP -> {
                    label.setTranslateX(0);
                    label.setTranslateY(-2 * MaryQubitApp.SCALE);
                }
                case DOWN -> {
                    label.setTranslateX(0);
                    label.setTranslateY(-9 * MaryQubitApp.SCALE);
                }
            }
        }

        public void setValue(double d) {
            valueProperty.set(d);
        }
    }

    public static class NumberedSpriteView extends SpriteView {
        protected final Label label = new Label();

        public NumberedSpriteView(Image spriteSheet, SpriteView following) {
            super(spriteSheet, following);
            label.textProperty().bind(number.asString());
            label.setFont(Font.font("Impact", 12 * MaryQubitApp.SCALE));
            getChildren().add(label);
        }
    }

    public int getNumber() {
        return number.get();
    }

    static Image loadImage(String url) {
        final var string = Optional.ofNullable(SpriteView.class.getResource(url))
                .map(URL::toString)
                .orElseThrow(() -> new IllegalStateException("Could not load " + url));
        final var requestedWidth = MaryQubitApp.SPRITE_SIZE * 3 * MaryQubitApp.SCALE;
        final var requestedHeight = MaryQubitApp.SPRITE_SIZE * 4 * MaryQubitApp.SCALE;
        return new Image(string, requestedWidth, requestedHeight, true, false);
    }

    private void visit() {
        MapObject object = MaryQubitApp.map[location.get().getX()][location.get().getY()];
        if (object != null) {
            object.visit(this);
        }
    }

    public final void startAnimation() {
        Timeline timeline = new Timeline(Animation.INDEFINITE,
                new KeyFrame(Duration.seconds(.25), new KeyValue(frame, 0)),
                new KeyFrame(Duration.seconds(.5), new KeyValue(frame, 1)),
                new KeyFrame(Duration.seconds(.75), new KeyValue(frame, 2)),
                new KeyFrame(Duration.seconds(1), new KeyValue(frame, 1))
        );
        timeline.onFinishedProperty().setValue(e -> timeline.play());
        timeline.play();
    }

    public void moveTo(MaryQubitApp.Location loc) {
        walking = new Timeline(Animation.INDEFINITE,
                new KeyFrame(Duration.seconds(.001), new KeyValue(direction, location.getValue().directionTo(loc))),
                new KeyFrame(Duration.seconds(.002), new KeyValue(location, loc)),
                new KeyFrame(Duration.seconds(1), new KeyValue(translateXProperty(), loc.getX() * MaryQubitApp.CELL_SIZE)),
                new KeyFrame(Duration.seconds(1), new KeyValue(translateYProperty(), loc.getY() * MaryQubitApp.CELL_SIZE)),
                new KeyFrame(Duration.seconds(.25), new KeyValue(frame, 0)),
                new KeyFrame(Duration.seconds(.5), new KeyValue(frame, 1)),
                new KeyFrame(Duration.seconds(.75), new KeyValue(frame, 2)),
                new KeyFrame(Duration.seconds(1), new KeyValue(frame, 1))
        );
        walking.setOnFinished(e -> visit());
        Platform.runLater(walking::play);
    }

    public SpriteView getFollowing() {
        return following;
    }

    public MaryQubitApp.Location getLocation() {
        return location.get();
    }

    public MaryQubitApp.Direction getDirection() {
        return direction.get();
    }
}
