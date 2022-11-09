package org.hzt.mockito;

import static org.hzt.utils.It.println;

public class DeliveryPlatform {
    public void deliver(Email email) {
        println("The email " + email + " is being delivered...");
    }
}
