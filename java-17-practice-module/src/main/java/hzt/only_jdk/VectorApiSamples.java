package hzt.only_jdk;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;

public final class VectorApiSamples {

    static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_PREFERRED;

    private VectorApiSamples() {
    }

    public static float[] scalarComputation(final float[] a, final float[] b) {
        final float[] c = createResultArrayIfArraysOfEqualLength(a, b);
        for (int i = 0; i < a.length; i++) {
            c[i] = (a[i] * a[i] + b[i] * b[i]) * -1.0F;
        }
        return c;
    }

    public static float[] vectorComputation(final float[] a, final float[] b) {
        final var c = createResultArrayIfArraysOfEqualLength(a, b);
        int i = 0;
        final int upperBound = SPECIES.loopBound(a.length);
        for (; i < upperBound; i += SPECIES.length()) {
            final var va = FloatVector.fromArray(SPECIES, a, i);
            final var vb = FloatVector.fromArray(SPECIES, b, i);
            final var vc = va.mul(va)
                    .add(vb.mul(vb))
                    .neg();
            vc.intoArray(c, i);
        }
        for (; i < a.length; i++) {
            c[i] = (a[i] * a[i] + b[i] * b[i]) * -1.0F;
        }
        return c;
    }

    private static float[] createResultArrayIfArraysOfEqualLength(final float[] a, final float[] b) {
        if (a.length != b.length) {
            throw new IllegalStateException("Length of input arrays must be equal");
        }
        return new float[a.length];
    }
}
