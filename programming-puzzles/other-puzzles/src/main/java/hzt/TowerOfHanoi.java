package hzt;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"squid:S106", "squid:S2234"})
public final class TowerOfHanoi {

    private static final int MAX_NUMBER_OF_DISKS_IN_GAME = 10;

    private TowerOfHanoi() {
    }

    public static void main(final String[] args) {
        for (int numberOfDisks = 1; numberOfDisks <= MAX_NUMBER_OF_DISKS_IN_GAME; numberOfDisks++) {
            System.out.printf("%nFor a game with %d disks:%n", numberOfDisks);
            final var moves = playTowerOfHanoiGame(numberOfDisks);
            moves.forEach(System.out::println);
            System.out.printf("%nTotal number of moves: %d%n", moves.size());
            System.out.println("----------------------------------------");
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
     * @param numberOfDisks the nr Of disks to move from rod a to rod c
     * @return the queue with the moves to take to set the disks in the target position
     */
    @NotNull
    static List<String> playTowerOfHanoiGame(final int numberOfDisks) {
        final List<String> moves = new ArrayList<>();
        moveDisk(numberOfDisks, 'a', 'c', 'b', moves);
        return moves;
    }

    private static void moveDisk(final int diskNumber,
                                 final char fromRod,
                                 final char targetRod,
                                 final char auxRod,
                                 final List<String> moves) {
        if (diskNumber == 1) {
            moves.add(String.format("Move disk %2d from rod %c to rod %c", diskNumber, fromRod, targetRod));
            return;
        }
        moveDisk(diskNumber - 1, fromRod, auxRod, targetRod, moves);
        moves.add(String.format("Move disk %2d from rod %c to rod %c", diskNumber, fromRod, targetRod));
        moveDisk(diskNumber - 1, auxRod, targetRod, fromRod, moves);
    }
}
