package org.hzt;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.sqrt;
import static org.hzt.utils.It.printf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RungeKuttaTest {

    /**
     * @see <a href="https://rosettacode.org/wiki/Runge-Kutta_method">Runge kutta method</a>
     */
    @Test
    void testCalculateOdeUsingRungeKutta() {
        final double dt = 0.10;
        final var solutionSize = 101;

        final var t_arr = new double[solutionSize];
        final var y_arr = new double[solutionSize];

        y_arr[0] = 1.0;

        RungeKutta.rungeKutta4((t, y) -> t * sqrt(y), t_arr, y_arr, dt);

        final List<Result> results = IntStream.range(0, t_arr.length)
                .filter(i -> i % 10 == 0)
                .mapToObj(i -> new Result(t_arr[i], y_arr[i]))
                .toList();

        results.forEach(Result::print);

        assertTrue(results.stream().allMatch(result -> result.error < 1e-4));
    }

    record Result(double t, double calc, double error) {
        Result(double t, double calc) {
            this(t, calc, calcErr(t, calc));
        }

        private static double calcErr(double t, double calc) {
            double actual = Math.pow(Math.pow(t, 2.0) + 4.0, 2) / 16.0;
            return Math.abs(actual - calc);
        }

        void print() {
            printf("y(%.1f) = %.8f Error: %.6f%n", t, calc, error);
        }
    }

}
