package hzt;

import org.hzt.utils.It;
import org.hzt.utils.PreConditions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static org.hzt.utils.It.printf;
import static org.hzt.utils.It.println;

public final class TowerOfHanoi {

    private TowerOfHanoi() {
    }

    public static void main(final String[] args) {
        final int MAX_NUMBER_OF_DISKS_IN_GAME = 10;
        for (int numberOfDisks = 1; numberOfDisks <= MAX_NUMBER_OF_DISKS_IN_GAME; numberOfDisks++) {
            printf("%nFor a game with %d disks:%n", numberOfDisks);
            final var moves = playTowerOfHanoiGame(numberOfDisks);
            moves.forEach(It::println);
            printf("%nTotal number of moves: %d%n", moves.size());
            println("----------------------------------------");
        }
    }

    /**
     * Example tower of Hanoi game with 3 disks
     * <p>
     * Initial state:                        Goal state:
     *      a      b      c                  a      b      c
     *     -|-     |      |                  |      |     -|-
     *    --|--    |      |                  |      |    --|--
     *   ---|---   |      |                  |      |   ---|---
     *
     * @see <a href="https://www.youtube.com/watch?v=8lhxIOAfDss">Recursion 'Super Power'</a>
     * @param numberOfDisks the nr Of disks to move from rod a to rod c
     * @return the queue with the moves to take to set the disks in the target position
     */
    @NotNull
    static List<String> playTowerOfHanoiGame(final int numberOfDisks) {
        final List<String> moves = new ArrayList<>();
        moveDisk(numberOfDisks, 'a', 'c', 'b', moves);
        return List.copyOf(moves);
    }

    private static void moveDisk(final int n,
                                 final char from,
                                 final char to,
                                 final char aux,
                                 final List<String> moves) {
        PreConditions.require(n >= 0, () -> "The nr of disks (n) can not be negative (n = " + n  + ")");
        if (n == 0) {
            return;
        }
        moveDisk(n - 1, from, aux, to, moves);
        moves.add("Move disk %2d from rod %c to rod %c".formatted(n, from, to));
        moveDisk(n - 1, aux, to, from, moves);
    }
}
