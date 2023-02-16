package hzt;

public class RaceConditionExampleThreads extends Thread{

    static int x;
    static int y;

    //The synchronized keyword is used wrong here
    @Override
    @SuppressWarnings("InfiniteLoopStatement")
    public synchronized void run() {
        for(;;) {
            x++;
            y++;
            System.out.println(x + " " + y);
            try {
                wait(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }

    }

    public static void main(String... args) {
        new RaceConditionExampleThreads().start();
        new RaceConditionExampleThreads().start();
    }
}
