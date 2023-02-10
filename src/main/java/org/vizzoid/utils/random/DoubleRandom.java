package org.vizzoid.utils.random;

import java.util.Random;

/**
 * Bounded Double Random
 */
public class DoubleRandom extends BoundedRandom<Double> {
    public DoubleRandom() {
    }

    public DoubleRandom(Double target, int maxTries) {
        super(target, maxTries);
    }

    public DoubleRandom(Double target, int maxTries, int minTries) {
        super(target, maxTries, minTries);
    }

    public DoubleRandom(Double bound) {
        super(bound);
    }

    public DoubleRandom(Double target, int maxTries, Double bound) {
        super(target, maxTries, bound);
    }

    public DoubleRandom(Double target, int maxTries, int minTries, Double bound) {
        super(target, maxTries, minTries, bound);
    }

    public DoubleRandom(Double origin, Double bound) {
        super(origin, bound);
    }

    public DoubleRandom(Double target, int maxTries, Double origin, Double bound) {
        super(target, maxTries, origin, bound);
    }

    public DoubleRandom(Double target, int maxTries, int minTries, Double origin, Double bound) {
        super(target, maxTries, minTries, origin, bound);
    }

    @Override
    public Double next() {
        return super.next();
    }

    @Override
    protected Double getFromRandom0(Random random) {
        return random.nextDouble();
    }

    @Override
    protected Double getFromRandom(Random random, Double bound) {
        return random.nextDouble(bound);
    }

    @Override
    protected Double getFromRandom(Random random, Double origin, Double bound) {
        return random.nextDouble(origin, bound);
    }
}
