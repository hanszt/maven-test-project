package org.hzt;

public final class MathUtils {

    private MathUtils() {
    }

    /**
     * From codebase of Quake III Arena
     * <p>
     * Original C code:
     *<pre>{@code
     * float Q_rsqrt( float number )
     * {
     * 	long i;
     * 	float x2, y;
     * 	const float threehalfs = 1.5F;
     *
     * 	x2 = number * 0.5F;
     * 	y  = number;
     * 	i  = * ( long * ) &y;                       // evil floating point bit level hacking
     * 	i  = 0x5f3759df - ( i >> 1 );               // what the fuck?
     * 	y  = * ( float * ) &i;
     * 	y  = y * ( threehalfs - ( x2 * y * y ) );   // 1st iteration
     *  //	y  = y * ( threehalfs - ( x2 * y * y ) );   // 2nd iteration, this can be removed
     *
     * 	return y;
     * }
     * }</pre>
     *
     * @param x the input param
     * @return the inverse square root of x
     *
     * @see <a href="https://stackoverflow.com/questions/11513344/how-to-implement-the-fast-inverse-square-root-in-java">
     *     How to implement the "fast inverse square root" in Java?</a>
     * @see <a href="https://en.wikipedia.org/wiki/Fast_inverse_square_root">Fast inverse square root</a>
     */
    public static float fastInverseSqrt(final float x) {
        final int ISR_FLOAT_CONSTANT = 0x5f_3759df;
        final float xHalf = 0.5F * x;
        int i = Float.floatToIntBits(x);
        i = ISR_FLOAT_CONSTANT - (i >> 1);
        float y = Float.intBitsToFloat(i);
        y *= (1.5F - xHalf * y * y);
        return y;
    }

    /**
     * Adaption for doubles
     *
     * @param x the input param
     * @return the inverse square root of x
     *
     * @see <a href="https://stackoverflow.com/questions/11513344/how-to-implement-the-fast-inverse-square-root-in-java">
     *     How to implement the "fast inverse square root" in Java?</a>
     */
    public static double fastInverseSqrt(final double x) {
        final long ISR_DOUBLE_CONSTANT = 0x5f_e6ec85e7de30daL;
        final double xHalf = 0.5D * x;
        long l = Double.doubleToLongBits(x);
        l = ISR_DOUBLE_CONSTANT - (l >> 1);
        double y = Double.longBitsToDouble(l);
        y *= (1.5D - xHalf * y * y);
        y *= (1.5D - xHalf * y * y);
        return y;
    }
}
