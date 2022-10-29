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

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

public abstract class MapObject extends SpriteView {

    boolean active = false;
    StrangeBridge strangeBridge;

    protected MapObject(Image spriteSheet, MaryQubitApp.Location loc, StrangeBridge strangeBridge) {
        super(spriteSheet, loc);
        this.strangeBridge = strangeBridge;
        MaryQubitApp.map[loc.cellX()][loc.cellY()] = this;
    }

    void setActive (boolean v) {
        this.active = v;
    }

    void toggleActive() {
        this.active = !this.active;
        if (active) {
            MaryQubitApp.setHelpText(getGateName()+" Gate activated!\nLambs visiting this gate will be affected");
            imageView.setEffect(new Glow(1));
        } else {
            MaryQubitApp.setHelpText("");
            imageView.setEffect(null);
        }

    }

    abstract String getGateName();

    public static class Barn extends MapObject {
        // Image by LovelyBlue: http://l0velyblue.deviantart.com/art/barncharset-350737104
        static final Image BARN = loadImage("/images/barn.png");
        public Barn(MaryQubitApp.Location loc, StrangeBridge s) {
            super(BARN, loc, s);
        }

        @Override
        String getGateName() {
            return "Barn";
        }

        @Override
        public void visit(SpriteView s) {
            if (s instanceof Mary mary) {
                Lamb lamb = new SpriteView.Lamb(s);
                mary.getAnimals().add(lamb);
                this.strangeBridge.addQubit(lamb);
                MaryQubitApp.setHelpText("Pressing X, H or C will\nactivate/deactivate the gates");

            }
        }
    }

    public static class Rainbow extends MapObject {
        static final Image RAINBOW = loadImage("/images/rainbow.png");
        private int c = 0;
        private int cId = 0;
        public Rainbow(MaryQubitApp.Location loc, StrangeBridge strangeBridge) {
            super(RAINBOW, loc, strangeBridge);
            startAnimation();
        }
        @Override
        public void visit(SpriteView s) {
            if (!active) {
                return;
            }
            if (s instanceof Lamb lamb) {
                int number=lamb.getNumber();
                if (c == 0) {
                    cId = number;
                    c = 1;
                } else if (number != cId){
                    this.strangeBridge.addCNot(cId, number);
                    c = 0;
                }
            }
        }

        @Override
        public String getGateName() {
            return "CNOT";
        }
    }

    public static class Church extends MapObject {
        // Image by LovelyBlue: http://l0velyblue.deviantart.com/art/Church-350736943
        static final Image CHURCH = loadImage("/images/church.png");
        LongProperty mealsServed = new SimpleLongProperty();
        public Church(MaryQubitApp.Location loc, StrangeBridge strangeBridge) {
            super(CHURCH, loc, strangeBridge);
            Label label = new Label();
            label.textProperty().bind(mealsServed.asString());
            label.setFont(Font.font("Impact", 12 * MaryQubitApp.SCALE));
            label.setTranslateX(-8 * MaryQubitApp.SCALE);
            label.setTranslateY(3 * MaryQubitApp.SCALE);
            getChildren().add(label);
        }

        @Override
        String getGateName() {
            return "Church";
        }

        @Override
        public void visit(SpriteView s) {
            if (s instanceof Mary mary) {
                long ir = strangeBridge.getLongResult();
                long total = mealsServed.get() + ir;
                mary.getAnimals().clear();
                strangeBridge.clearProgram();
                mealsServed.setValue(total);
            }
        }

    }

    public static class ChickenCoop extends MapObject {
        // Image by LovelyBlue: http://l0velyblue.deviantart.com/art/chickencoop-350736803
        static final Image CHICKEN_COOP = loadImage("/images/chicken-coop.png");
        public ChickenCoop(MaryQubitApp.Location loc, StrangeBridge strangeBridge) {
            super(CHICKEN_COOP, loc, strangeBridge);
        }
        @Override
        public void visit(SpriteView s) {
            System.err.println("X visit by "+s+" and me active? "+active);
            if (!active) {
                return;
            }
            if (s instanceof Lamb lamb) {
                int number=lamb.getNumber();
                this.strangeBridge.addX(number);
            }
        }

        @Override
        public String getGateName() {
            return "X";
        }
    }

    public static class Nest extends MapObject {
        // Image derived from Lokilech's Amselnest: http://commons.wikimedia.org/wiki/File:Amselnest_lokilech.jpg
        static final Image NEST = loadImage("/images/nest.png");
        public Nest(MaryQubitApp.Location loc, StrangeBridge strangeBridge) {
            super(NEST, loc, strangeBridge);
        }
        @Override
        public void visit(SpriteView s) {
            System.err.println("H visit by "+s+" and me active? "+active);
            if (!active) {
                return;
            }
            if (s instanceof Lamb) {
                Lamb lamb = (Lamb)s;
                int number=lamb.getNumber();
                this.strangeBridge.addH(number);
            }
        }

        @Override
        public String getGateName() {
            return "H";
        }
    }

    public static class Fox extends MapObject {
        // Image by PinedaVX: http://www.rpgmakervx.net/index.php?showtopic=9422
        static final Image FOX = loadImage("/images/fox.png");
        public  Fox(MaryQubitApp.Location loc, StrangeBridge strangeBridge) {
            super(FOX, loc, strangeBridge);
            startAnimation();
        }

        @Override
        String getGateName() {
            return "Fox";
        }

        @Override
        public void visit(SpriteView s) {
            System.err.println("Fox visited by "+s);
            if (s instanceof Mary mary) {
                mary.getAnimals().clear();
                this.strangeBridge.clearProgram();
            }
        }
    }

    public abstract void visit(SpriteView shepherd);
}
