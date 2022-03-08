package hzt.inheritance;

import java.io.IOException;

public class PortConnector {

    public PortConnector(int port) throws IOException {
        if (port < 0) {
            throw new IOException();
        }
    }

    public void connect(String number) throws IOException {
        if (number.isEmpty()) {
            throw new IOException();
        }
    }
    // Source: Enthuware Java11OCP Test 1 Q 37
}
