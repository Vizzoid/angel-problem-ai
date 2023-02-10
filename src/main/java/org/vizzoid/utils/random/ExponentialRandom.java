package org.vizzoid.utils.random;

import java.util.Random;

/**
 * Controlled Exponential Random
 */
public class ExponentialRandom extends ControlledRandom<Double> {
    public ExponentialRandom() {
    }

    public ExponentialRandom(Double target, int maxTries) {
        super(target, maxTries);
    }

    public ExponentialRandom(Double target, int maxTries, int minTries) {
        super(target, maxTries, minTries);
    }

    @Override
    public Double next() {
        return super.next();
    }

    @Override
    protected Double getFromRandom(Random random) {
        return random.nextExponential();
    }
}
