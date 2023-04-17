package org.hzt;

import java.util.function.UnaryOperator;

/**
 * @see <a href="https://rosettacode.org/wiki/Runge-Kutta_method">Runge kutta method</a>
 */
public final class RungeKutta {

    private RungeKutta() {
    }

    public static double[] rungeKutta4ChatGpt(double[] vector, double h, UnaryOperator<double[]> solveOdes) {
        double[] k1 = solveOdes.apply(vector);
        double[] k2 = solveOdes.apply(addElementWise(vector, k1, h / 2));
        double[] k3 = solveOdes.apply(addElementWise(vector, k2, h / 2));
        double[] k4 = solveOdes.apply(addElementWise(vector, k3, h));

        final var resultVector = new double[vector.length];
        // Update the variables
        for (int i = 0; i < vector.length; i++) {
            resultVector[i] = vector[i] + (k1[i] + 2 * k2[i] + 2 * k3[i] + k4[i]) * h / 6;
        }
        return resultVector;
    }

    // Helper method to add two arrays element-wise
    private static double[] addElementWise(double[] x, double[] k, double h) {
        double[] result = new double[x.length];
        for (int i = 0; i < x.length; i++) {
            result[i] = x[i] + k[i] * h;
        }
        return result;
    }
}
