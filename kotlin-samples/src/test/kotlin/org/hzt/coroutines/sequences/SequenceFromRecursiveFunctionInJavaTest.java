package org.hzt.coroutines.sequences;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.hzt.coroutines.sequences.poc.MySequencesKt;
import org.hzt.coroutines.sequences.poc.SequenceScope;
import org.hzt.utils.PreConditions;
import org.hzt.utils.sequences.Sequence;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.*;

class SequenceFromRecursiveFunctionInJavaTest {

    /**
     * This implementation does not produce the right result. See org.hzt.coroutines.sequences.TowerOfHanoiTest for a working example
     * <p>
     * I'm afraid you're asking for the impossible. Kotlin doesn't implement coroutines through some backdoor JVM magic,
     * but through the transformation of regular Java methods.
     * Suspendable functions involve a hidden method signature change and their return type changes as well.
     *
     * @see <a href="https://stackoverflow.com/questions/48328239/how-to-build-a-sequence-from-an-external-non-suspending-function">
     * How to build a sequence from an external non-suspending function</a>
     */
    @Test
    void testYieldASequenceFromARecursiveFunctionInJavaIsNotPossible() {
        final var nrOfDisks = 3;

        final var instructions =
                stringSequence((scope, continuation) -> moveDisk(scope, 3, 'a', 'c', 'b', continuation))
                        .toList();


        final var expectedNrOfMoves = (int) (Math.pow(2, nrOfDisks) - 1);

        final var expected = List.of(
                "Move disk  1 from rod a to rod c",
                "Move disk  2 from rod a to rod b",
                "Move disk  1 from rod c to rod b",
                "Move disk  3 from rod a to rod c",
                "Move disk  1 from rod b to rod a",
                "Move disk  2 from rod b to rod c",
                "Move disk  1 from rod a to rod c");

        //If it worked well this, should be two passing equals assertions
        assertAll(
                () -> assertNotEquals(expectedNrOfMoves, instructions.size()),
                () -> assertNotEquals(expected, instructions)
        );
    }

    private Sequence<String> stringSequence(BiConsumer<SequenceScope<String>, Continuation<? super Unit>> block) {
        return () -> MySequencesKt.iterator((scope, continuation) -> {
            block.accept(scope, continuation);
            return Unit.INSTANCE;
        });
    }

    private void moveDisk(
            final SequenceScope<String> scope,
            final int nrOfDisks,
            final char fromRod,
            final char targetRod,
            final char auxRod,
            final Continuation<? super Unit> continuation) {
        PreConditions.require(nrOfDisks >= 0, () -> "The nr of disks (n) can not be negative (n = " + nrOfDisks  + ")");
        if (nrOfDisks == 0) {
            return;
        }
        moveDisk(scope, nrOfDisks - 1, fromRod, auxRod, targetRod, continuation);
        scope.yield(String.format("Move disk %2d from rod %c to rod %c", nrOfDisks, fromRod, targetRod), continuation);
        moveDisk(scope, nrOfDisks - 1, auxRod, targetRod, fromRod, continuation);
    }

    @Test
    void testImplementationInKotlinAndThenCallingFromJavaWorks() {
        final var nrOfDisks = 3;

        final var instructionsFromKtFile = TowerOfHanoiKt.towerOfHanoiStream(nrOfDisks).toList();

        final var expectedNrOfMoves = (int) (Math.pow(2, nrOfDisks) - 1);

        final var expected = List.of(
                "Move disk  1 from rod a to rod c",
                "Move disk  2 from rod a to rod b",
                "Move disk  1 from rod c to rod b",
                "Move disk  3 from rod a to rod c",
                "Move disk  1 from rod b to rod a",
                "Move disk  2 from rod b to rod c",
                "Move disk  1 from rod a to rod c");

        assertAll(
                () -> assertEquals(expectedNrOfMoves, instructionsFromKtFile.size()),
                () -> assertEquals(expected, instructionsFromKtFile)
        );
    }
}
