package hzt.only_jdk;

public class VirtualThreadSample {

    public static void main(String[] args) {

        final var virtualThread = Thread.ofVirtual()
                .factory()
                .newThread(() -> System.out.println(Thread.currentThread()));

        final var kernelThread = Thread.ofPlatform()
                .factory()
                .newThread(() -> System.out.println(Thread.currentThread()));

        virtualThread.start();
        kernelThread.start();
    }
}
