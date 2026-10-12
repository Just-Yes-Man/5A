package simpson;

/**
 * Funcion gamma para enteros y semi-enteros (x = n o x = n + 1/2),
 * usando G(x) = (x-1) * G(x-1), G(1) = 1, G(1/2) = sqrt(pi).
 */
public final class Gamma {

    private static final double EPS = 1e-9;

    private Gamma() { }

    public static double of(double x) {
        if (x <= 0) {
            throw new IllegalArgumentException("x debe ser > 0: " + x);
        }
        if (Math.abs(x - 1.0) < EPS) {
            return 1.0;
        }
        if (Math.abs(x - 0.5) < EPS) {
            return Math.sqrt(Math.PI);
        }
        if (x < 1.0) {
            throw new IllegalArgumentException("Solo enteros o semi-enteros: " + x);
        }
        return (x - 1.0) * of(x - 1.0);
    }
}
