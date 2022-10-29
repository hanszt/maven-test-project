package org.hzt;

import org.junit.jupiter.api.Test;
import org.redfx.strange.Qubit;

import static org.junit.jupiter.api.Assertions.*;

class StrangeBridgeTest {

    @Test
    void testQubitsMeasureLongResult() {
        final var qubit = new Qubit();
        qubit.setMeasuredValue(true);
        final Qubit[] qubits = {new Qubit(), qubit, new Qubit(), qubit};

        final var intResult = StrangeBridge.calculateIntResult(qubits);

        System.out.println(intResult);

        assertEquals(5, intResult);
    }

}
