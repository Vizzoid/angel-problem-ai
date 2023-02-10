package org.vizzoid.utils.random;

import java.util.Random;

/**
 * Bounded Gaussian Random
 */
public class GaussianRandom extends BoundedRandom<Double> {
    public GaussianRandom() {
    }

    public GaussianRandom(Double target, int maxTries) {
        super(target, maxTries);
    }

    public GaussianRandom(Double target, int maxTries, int minTries) {
        super(target, maxTries, minTries);
    }

    public GaussianRandom(Double mean, Double stddev) {
        super(mean, stddev);
    }

    public GaussianRandom(Double target, int maxTries, Double mean, Double stddev) {
        super(target, maxTries, mean, stddev);
    }

    public GaussianRandom(Double target, int maxTries, int minTries, Double mean, Double stddev) {
        super(target, maxTries, minTries, mean, stddev);
    }

    @Override
    public Double next() {
        return super.next();
    }

    public double next(double mean, double standardDeviation) {
        return next() * standardDeviation + mean;
    }

    @Override
    protected Double getFromRandom0(Random random) {
        return random.nextGaussian();
    }

    @Override
    protected Double getFromRandom(Random random, Double bound) {
        return null;
    }

    @Override
    protected Double getFromRandom(Random random, Double mean, Double stddev) {
        return random.nextGaussian(mean, stddev);
    }
}
