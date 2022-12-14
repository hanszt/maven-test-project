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

import javafx.scene.Group;
import org.redfx.strange.Complex;
import org.redfx.strange.Program;
import org.redfx.strange.QuantumExecutionEnvironment;
import org.redfx.strange.Qubit;
import org.redfx.strange.Result;
import org.redfx.strange.Step;
import org.redfx.strange.gate.Cnot;
import org.redfx.strange.gate.Hadamard;
import org.redfx.strange.gate.Identity;
import org.redfx.strange.gate.X;
import org.redfx.strange.local.SimpleQuantumExecutionEnvironment;
import org.redfx.strangefx.render.Renderer;
import org.redfx.strangefx.ui.QubitBoard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StrangeBridge extends Group {

    private static final Logger LOGGER = LoggerFactory.getLogger(StrangeBridge.class);

    private static final String SHOULD_NOT_BE_CALLED_ON_THREAD_INSTANCES = "squid:S2236";
    private Program program;
    private final List<SpriteView.Lamb> qubitLamb = new LinkedList<>();
    private final Thread measureThread;
    private Result result;
    private QubitBoard board;

    public StrangeBridge() {
        this.program = new Program(0);
        measureThread = new Thread(this::measure);
    }

    public void addQubit(SpriteView.Lamb lamb) {
        int nc = program.getNumberQubits() + 1;
        List<Step> oldSteps = program.getSteps();
        Program newProgram = new Program(nc);
        for (Step s : oldSteps) {
            newProgram.addStep(s);
        }
        this.program = newProgram;
        Step s1 = new Step();
        s1.addGate(new Identity(nc - 1));
        this.program.addStep(s1);
        QuantumExecutionEnvironment simulator = new SimpleQuantumExecutionEnvironment();
        simulator.runProgram(this.program);
        this.getChildren().clear();
        this.getChildren().add(Renderer.getRenderGroup(this.program));
        qubitLamb.add(lamb);
        renderProgram();
    }

    public void addH(int nr) {
        LOGGER.info("add H to {}", nr);
        Step s = new Step();
        s.addGate(new Hadamard(nr));
        this.program.addStep(s);
        renderProgram();
    }

    public void addX(int nr) {
        LOGGER.info("add X to {}", nr);
        Step s = new Step();
        s.addGate(new X(nr));
        this.program.addStep(s);
        renderProgram();
    }

    public void addCNot(int q1, int q2) {
        LOGGER.info("Add CNot for {}, {}", q1, q2);
        Step s = new Step();
        s.addGate(new Cnot(q1, q2));
        LOGGER.info("created step has gates {}", s.getGates());
        this.program.addStep(s);
        renderProgram();
    }

    @SuppressWarnings(SHOULD_NOT_BE_CALLED_ON_THREAD_INSTANCES)
    private synchronized void renderProgram() {
        final var simulator = new SimpleQuantumExecutionEnvironment();
        this.result = simulator.runProgram(this.program);
        this.getChildren().clear();
        if (board != null) {
            Renderer.disable(board);
        }
        board = Renderer.getRenderGroup(this.program);
        this.getChildren().add(board);
        if (measureThread.getState() == Thread.State.NEW) {
            measureThread.start();
        }
        synchronized (measureThread) {
            measureThread.notifyAll();
        }
    }

    public void clearProgram() {
        this.program.getSteps().clear();
        this.program = new Program(0);
        this.qubitLamb.clear();
        renderProgram();
    }

    public int getLongResult() {
        Qubit[] qubits = this.result.getQubits();
        return calculateIntResult(qubits);
    }

    static int calculateIntResult(Qubit[] qubits) {
        int intResult = 0;
        for (Qubit q : qubits) {
            intResult = 2 * intResult;
            intResult = intResult + q.measure();
        }
        return intResult;
    }

    public void measure() {
        //noinspection InfiniteLoopStatement
        while (true) {
            Qubit[] qubits;
            Complex[] probs;
            int probCount = 0;

            //noinspection SynchronizeOnNonFinalField
            synchronized (result) {
                result.measureSystem();
                qubits = result.getQubits();
                probs = result.getProbability();
                result.printInfo();
                for (Complex prob : probs) {
                    if (Double.compare(prob.abssqr(), 0.0) != 0) {
                        probCount++;
                    }
                }
            }
            int i = 0;
            for (Qubit q : qubits) {
                SpriteView.Lamb target = qubitLamb.get(i++);
                target.setValue(q.measure());
            }
            try {
                if (probCount == 1) {
                    // no other option, so wait for changes
                    synchronized (this) {
                        this.wait();
                    }
                } else { // more than one option, allow to render others
                    TimeUnit.MILLISECONDS.sleep(250);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }


}
