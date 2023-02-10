package org.vizzoid.utils.random;

import java.util.Random;

/**
 * Bounded Long Random
 */
public class LongRandom extends BoundedRandom<Long> {
    public LongRandom() {
    }

    public LongRandom(Long target, int maxTries) {
        super(target, maxTries);
    }

    public LongRandom(Long target, int maxTries, int minTries) {
        super(target, maxTries, minTries);
    }

    public LongRandom(Long bound) {
        super(bound);
    }

    public LongRandom(Long target, int maxTries, Long bound) {
        super(target, maxTries, bound);
    }

    public LongRandom(Long target, int maxTries, int minTries, Long bound) {
        super(target, maxTries, minTries, bound);
    }

    public LongRandom(Long origin, Long bound) {
        super(origin, bound);
    }

    public LongRandom(Long target, int maxTries, Long origin, Long bound) {
        super(target, maxTries, origin, bound);
    }

    public LongRandom(Long target, int maxTries, int minTries, Long origin, Long bound) {
        super(target, maxTries, minTries, origin, bound);
    }

    @Override
    public Long next() {
        return super.next();
    }

    @Override
    protected Long getFromRandom0(Random random) {
        return random.nextLong();
    }

    @Override
    protected Long getFromRandom(Random random, Long bound) {
        return random.nextLong(bound);
    }

    @Override
    protected Long getFromRandom(Random random, Long origin, Long bound) {
        return random.nextLong(origin, bound);
    }
}
