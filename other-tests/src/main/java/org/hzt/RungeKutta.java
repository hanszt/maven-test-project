package org.hzt;

import java.util.function.DoubleBinaryOperator;

/**
 * @see <a href="https://rosettacode.org/wiki/Runge-Kutta_method">Runge kutta method</a>
 */
public final class RungeKutta {

    private RungeKutta() {
    }

    static void rungeKutta4(DoubleBinaryOperator yp_func, double[] t, double[] y, double dt) {

        for (int n = 0; n < t.length - 1; n++) {
            final double dy1 = dt * yp_func.applyAsDouble(t[n], y[n]);
            final double dy2 = dt * yp_func.applyAsDouble(t[n] + dt / 2.0, y[n] + dy1 / 2.0);
            final double dy3 = dt * yp_func.applyAsDouble(t[n] + dt / 2.0, y[n] + dy2 / 2.0);
            final double dy4 = dt * yp_func.applyAsDouble(t[n] + dt, y[n] + dy3);
            t[n + 1] = t[n] + dt;
            y[n + 1] = y[n] + (dy1 + 2.0 * (dy2 + dy3) + dy4) / 6.0;
        }

    }
}
