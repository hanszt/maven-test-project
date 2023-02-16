package hzt.preview.generators;

import java.time.Duration;

public class TowerOfHanoi {

    public static void main(String... args) {
        final var nrOfDisks = 6;
        Generator.<String>from(scope -> moveDisk(scope, nrOfDisks, 'a', 'c', 'b'))
                .consumeAsStream(s -> s
                        .peek(e -> sleep(Duration.ofMillis(200)))
                        .forEach(System.out::println)
                );
    }

    private static void sleep(Duration duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void moveDisk(
            final GeneratorScope<String> scope,
            final int diskNumber,
            final char fromRod,
            final char targetRod,
            final char auxRod) {
        if (diskNumber == 1) {
            scope.yieldNext(String.format("Move disk %2d from rod %c to rod %c", diskNumber, fromRod, targetRod));
            return;
        }
        moveDisk(scope, diskNumber - 1, fromRod, auxRod, targetRod);
        scope.yieldNext(String.format("Move disk %2d from rod %c to rod %c", diskNumber, fromRod, targetRod));
        moveDisk(scope, diskNumber - 1, auxRod, targetRod, fromRod);
    }
}
