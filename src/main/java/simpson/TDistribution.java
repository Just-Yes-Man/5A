package simpson;

import java.util.function.DoubleUnaryOperator;


public final class TDistribution implements DoubleUnaryOperator {

    private final int dof;
    private final double constant;

    public TDistribution(int dof) {
        if (dof <= 0) {
            throw new IllegalArgumentException("dof debe ser > 0: " + dof);
        }
        this.dof = dof;
        this.constant = Gamma.of((dof + 1) / 2.0)
                / (Math.sqrt(dof * Math.PI) * Gamma.of(dof / 2.0));
    }

    public double constant() {
        return constant;
    }

    @Override
    public double applyAsDouble(double x) {
        return constant * Math.pow(1.0 + x * x / dof, -(dof + 1) / 2.0);
    }
}
