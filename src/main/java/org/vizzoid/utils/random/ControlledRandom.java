package org.vizzoid.utils.random;

import java.util.Random;

/**
 * This random works in that, if the currentTry is above to the max tries, it will return the target
 * EX: If max tries is 5, on the 6th try it will 100% return the target
 */
public abstract class ControlledRandom<E> extends InputOutputRandom<E, E> {

    public ControlledRandom() {
        super();
    }

    public ControlledRandom(E target, int maxTries) {
        super(target, maxTries);
    }

    public ControlledRandom(E target, int maxTries, int minTries) {
        super(target, maxTries, minTries);
    }

    protected ControlledRandom(E target, int maxTries, boolean controlled) {
        super(target, maxTries, controlled);
    }

    protected ControlledRandom(E target, int maxTries, int minTries, boolean controlled) {
        super(target, maxTries, minTries, controlled);
    }

    protected abstract E getFromRandom(Random random);

}
