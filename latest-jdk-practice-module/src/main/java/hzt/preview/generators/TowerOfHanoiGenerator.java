package hzt.preview.generators;

import org.hzt.utils.It;
import org.hzt.utils.TimingUtils;

import java.time.Duration;

public class TowerOfHanoiGenerator {

    private static final String PARAMETERS_SHOULD_BE_PASSED_IN_CORRECT_ORDER = "java:S2234";
    private static final String DO_NOT_USE_PEEK_IN_PRODUCTION = "java:S3864";

    @SuppressWarnings(DO_NOT_USE_PEEK_IN_PRODUCTION)
    public static void main(String... args) {
        final var nrOfDisks = 6;
        Generator.<String>yieldingFrom(scope -> moveDisk(scope, nrOfDisks, 'a', 'c', 'b'))
                .consumeAsStream(s -> s
                        .peek(e -> TimingUtils.sleep(Duration.ofMillis(200)))
                        .forEach(It::println)
                );
    }

    /**
     * @param scope The generator scope that will yield the values
     * @param n the nr of disks
     * @param from The from-rod
     * @param to The to-rod
     * @param aux the helper-rod
     */
    @SuppressWarnings({PARAMETERS_SHOULD_BE_PASSED_IN_CORRECT_ORDER})
    static void moveDisk(
            final GeneratorScope<String> scope,
            final int n,
            final char from,
            final char to,
            final char aux) {
        if (n == 0) {
            return;
        }
        moveDisk(scope, n - 1, from, aux, to);
        scope.yieldNext("Move disk %2d from rod %c to rod %c".formatted(n, from, to));
        moveDisk(scope, n - 1, aux, to, from);
    }
}
