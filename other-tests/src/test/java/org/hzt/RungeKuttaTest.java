package org.hzt;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @see <a href="https://rosettacode.org/wiki/Runge-Kutta_method">Runge kutta method</a>
 */
class RungeKuttaTest {

    /**
     * Asked to chat gpt on 2023-04-07
     */
    @Test
    void testRungeKuttaChatGptRefactored() {
        // Initial conditions
        double[] initial = {1.0e11, 0, 0, 3.0e4}; // x = 100 billion meters, y = 0, vx = 0, vy = 30,000 m/s
        double dt = 1000; // time step in seconds
        // Number of steps
        int numSteps = 100_000;

        double[] current = initial;
        // Time integration loop
        for (int i = 0; i < numSteps; i++) {
            current = RungeKutta.rungeKutta4ChatGpt(current, dt, RungeKuttaTest::deltaDynamicsVarsUsingNewtonGravitationalLaw);
        }
        assertEquals("[-6.614038353504153E19, -6.61270854965229E15, -6.612053906103317E11, -6.6107356016833246E7]", Arrays.toString(current));
    }

    /**
     * If you want to compute the motion of planets in a two-dimensional plane, you can simplify the equations by considering only the x and y coordinates, assuming the motion is constrained to the plane.
     * <p>
     * Here's an updated version of the steps to solve the second-order differential equation for the motion of planets in a 2D plane:
     * <p>
     * Define the variables: Identify the variables in the problem, such as the positions and velocities of the planets in the x and y coordinates, and assign symbols to represent them. Let's denote the position of a planet as vector r(t) = (x(t), y(t)), where x and y are the Cartesian coordinates, and t represents time.
     * <p>
     * Formulate the ODEs: Write down the second-order ODEs that describe the motion of the planets in the x and y coordinates. These ODEs are based on Newton's law of gravitation and can be formulated using the following equations:
     * <p>
     * d^2x/dt^2 = -G * (m1 + m2) * x / r^2
     * <p>
     * d^2y/dt^2 = -G * (m1 + m2) * y / r^2
     * <p>
     * where G is the gravitational constant, m1 and m2 are the masses of the two objects (e.g., the Sun and the planet), and r is the distance between them given by r = sqrt(x^2 + y^2).
     * <p>
     * Convert to first-order ODEs: Introduce new variables to convert the second-order ODEs into a system of first-order ODEs. Let's define the velocities of the planets in the x and y directions as v(t) = (vx(t), vy(t)), where vx and vy are the components of the velocity vector. Then we can rewrite the ODEs as a system of first-order ODEs:
     * <p>
     * dx/dt = vx
     * <p>
     * dy/dt = vy
     * <p>
     * dvx/dt = -G * (m1 + m2) * x / r^2
     * <p>
     * dvy/dt = -G * (m1 + m2) * y / r^2
     * <p>
     * Choose initial conditions: Specify the initial conditions for the variables x, y, vx, and vy, which represent the initial positions and velocities of the planets in the x and y coordinates. These initial conditions are typically obtained from observational data or numerical simulations.
     * <p>
     * Select a numerical integration method: Choose a numerical integration method, such as the Euler method, the Verlet method, or the Runge-Kutta method, to numerically solve the system of first-order ODEs. These methods allow you to approximate the solutions of the ODEs over a discrete time grid.
     * <p>
     * Implement the numerical solver: Write a computer program or use a mathematical software package, such as Python with a numerical library like NumPy or MATLAB, to implement the numerical solver based on the selected integration method. Use the initial conditions, the ODEs, and the numerical solver to compute the positions and velocities of the planets over time in the x and y coordinates.
     * <p>
     * Analyze and interpret the results: Once the numerical solver has computed the positions and velocities of the planets, you can analyze and interpret the results. You can study the motion of the planets, such as their orbits, speeds, and positions relative to other celestial bodies, to gain insights into the dynamics of the solar system in the 2D plane.
     * <p>
     * Remember that this is a simplified model and may not capture all the complexities of the motion of planets in a real solar system. Depending on the specific problem you are trying to solve, you may need to consider additional factors, such as the eccentricity of orbits, perturbations from other celestial bodies,
     */
    // Define the ODEs
    // Assumes x[0] = x, x[1] = y, x[2] = vx, x[3] = vy
    private static double[] deltaDynamicsVarsUsingNewtonGravitationalLaw(double[] vector) {
        double G = 6.67430e-11; // gravitational constant
        double m1 = 1.989e30;   // mass of the Sun
        double m2 = 5.972e24;   // mass of the planet

        final var x = vector[0];
        final var y = vector[1];
        final var vx = vector[2];
        final var vy = vector[3];

        double r = Math.sqrt(x * x + y * y); // distance between the two objects

        double[] ddt = new double[vector.length];
        ddt[0] = vx; // dx/dt = vx
        ddt[1] = vy; // dy/dt = vy
        ddt[2] = (-G * (m1 + m2) * x) / (r * r); // dvx/dt = -G * (m1 + m2) * x / r^2
        ddt[3] = (-G * (m1 + m2) * y) / (r * r); // dvy/dt = -G * (m1 + m2) * y / r^2

        return ddt;
    }

}
