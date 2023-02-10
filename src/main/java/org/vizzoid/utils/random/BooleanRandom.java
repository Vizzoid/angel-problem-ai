package org.vizzoid.utils.random;

import java.util.Random;

/**
 * Controlled Boolean Random
 */
public class BooleanRandom extends ControlledRandom<Boolean> {
    public BooleanRandom() {
    }

    public BooleanRandom(Boolean target, int maxTries) {
        super(target, maxTries);
    }

    @Override
    public Boolean next() {
        return super.next();
    }

    @Override
    protected Boolean getFromRandom(Random random) {
        return random.nextBoolean();
    }
}
