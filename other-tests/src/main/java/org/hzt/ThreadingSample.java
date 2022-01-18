package org.hzt;

public class ThreadingSample {

    public static void main(String[] args) {
        int counter = 0;
        while (counter < 10) {
            try {
                System.out.println("counter = " + counter);
                Thread.sleep(1000);
            } catch (InterruptedException ee) {
                Thread.currentThread().interrupt();
            }
            counter++;
        }
    }
}
