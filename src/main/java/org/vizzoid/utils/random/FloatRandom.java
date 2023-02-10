package org.vizzoid.utils.random;

import java.util.Random;

/**
 * Bounded Float Random
 */
public class FloatRandom extends BoundedRandom<Float> {
    public FloatRandom() {
    }

    public FloatRandom(Float target, int maxTries) {
        super(target, maxTries);
    }

    public FloatRandom(Float target, int maxTries, int minTries) {
        super(target, maxTries, minTries);
    }

    public FloatRandom(Float bound) {
        super(bound);
    }

    public FloatRandom(Float target, int maxTries, Float bound) {
        super(target, maxTries, bound);
    }

    public FloatRandom(Float target, int maxTries, int minTries, Float bound) {
        super(target, maxTries, minTries, bound);
    }

    public FloatRandom(Float origin, Float bound) {
        super(origin, bound);
    }

    public FloatRandom(Float target, int maxTries, Float origin, Float bound) {
        super(target, maxTries, origin, bound);
    }

    public FloatRandom(Float target, int maxTries, int minTries, Float origin, Float bound) {
        super(target, maxTries, minTries, origin, bound);
    }

    @Override
    public Float next() {
        return super.next();
    }

    @Override
    protected Float getFromRandom0(Random random) {
        return random.nextFloat();
    }

    @Override
    protected Float getFromRandom(Random random, Float bound) {
        return random.nextFloat(bound);
    }

    @Override
    protected Float getFromRandom(Random random, Float origin, Float bound) {
        return random.nextFloat(origin, bound);
    }
}
