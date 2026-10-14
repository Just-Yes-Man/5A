package simpson;

import java.util.function.DoubleUnaryOperator;


public final class Simpson {

    public static final int DEFAULT_SEGMENTS = 10;
    public static final double DEFAULT_ERROR = 0.00001;

    private Simpson() { }

    /** Una sola pasada con num_seg segmentos (num_seg par). */
    public static double integrate(DoubleUnaryOperator f, double x, int numSeg) {
        if (numSeg <= 0 || numSeg % 2 != 0) {
            throw new IllegalArgumentException("num_seg debe ser par y > 0: " + numSeg);
        }
        double w = x / numSeg;
        double sum = f.applyAsDouble(0) + f.applyAsDouble(x);
        for (int i = 1; i < numSeg; i++) {
            double mult = (i % 2 == 1) ? 4 : 2;
            sum += mult * f.applyAsDouble(i * w);
        }
        return (w / 3.0) * sum;
    }

    /** Duplica num_seg hasta que |nuevo - anterior| < e. */
    public static double integrate(DoubleUnaryOperator f, double x, int numSeg, double e) {
        if (x < 0) {
            throw new IllegalArgumentException("x debe ser >= 0: " + x);
        }
        return integrate(f, 0, x, numSeg, e);
    }

    /** Integra en el intervalo [lower, upper], refinando hasta alcanzar e. */
    public static double integrate(
            DoubleUnaryOperator f, double lower, double upper, int numSeg, double e) {
        if (lower == upper) {
            return 0.0;
        }
        if (e <= 0) {
            throw new IllegalArgumentException("e debe ser > 0: " + e);
        }
        double previous = integrate(f, lower, upper, numSeg);
        numSeg *= 2;
        double current = integrate(f, lower, upper, numSeg);
        while (Math.abs(current - previous) >= e) {
            previous = current;
            numSeg *= 2;
            current = integrate(f, lower, upper, numSeg);
        }
        return current;
    }

    /** Una sola pasada en el intervalo [lower, upper]. */
    public static double integrate(
            DoubleUnaryOperator f, double lower, double upper, int numSeg) {
        if (numSeg <= 0 || numSeg % 2 != 0) {
            throw new IllegalArgumentException("num_seg debe ser par y > 0: " + numSeg);
        }
        double w = (upper - lower) / numSeg;
        double sum = f.applyAsDouble(lower) + f.applyAsDouble(upper);
        for (int i = 1; i < numSeg; i++) {
            double mult = (i % 2 == 1) ? 4 : 2;
            sum += mult * f.applyAsDouble(lower + i * w);
        }
        return (w / 3.0) * sum;
    }

    /** Integral de la t de 0 a x con valores por defecto. */
    public static double tIntegral(double x, int dof) {
        if (x < 0) {
            throw new IllegalArgumentException("x debe ser >= 0: " + x);
        }
        return tIntegral(0, x, dof);
    }

    /** Integral de la t en el intervalo [lower, upper] con valores por defecto. */
    public static double tIntegral(double lower, double upper, int dof) {
        return integrate(new TDistribution(dof), lower, upper, DEFAULT_SEGMENTS, DEFAULT_ERROR);
    }
}
