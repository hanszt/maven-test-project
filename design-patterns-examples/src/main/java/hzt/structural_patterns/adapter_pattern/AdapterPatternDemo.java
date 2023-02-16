package hzt.structural_patterns.adapter_pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdapterPatternDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdapterPatternDemo.class);

    public static void main(String... args) {
        // Round fits round, no surprise.
        RoundHole hole = new RoundHole(5);
        RoundPeg roundPeg = new RoundPeg(5);
        if (hole.fits(roundPeg)) {
            LOGGER.info("Round peg with radius of 5 fits round hole with radius of 5.");
        }

        SquarePeg smallSqPeg = new SquarePeg(2);
        SquarePeg largeSqPeg = new SquarePeg(20);
        // hole.fits(smallSqPeg); // Won't compile.

        // Adapter solves the problem.
        SquarePegAdapter smallSqPegAdapter = new SquarePegAdapter(smallSqPeg);
        SquarePegAdapter largeSqPegAdapter = new SquarePegAdapter(largeSqPeg);
        if (hole.fits(smallSqPegAdapter)) {
            LOGGER.info("Square peg with width of 2 fits round hole with radius of 5.");
        }
        if (!hole.fits(largeSqPegAdapter)) {
            LOGGER.info("Square peg with width of 20 does not fit into round hole with radius of 5.");
        }
    }
}
