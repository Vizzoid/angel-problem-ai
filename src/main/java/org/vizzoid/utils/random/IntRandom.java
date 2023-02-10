package org.vizzoid.utils.random;

import java.util.Random;

/**
 * Bounded Int Random
 */
public class IntRandom extends BoundedRandom<Integer> {
    public IntRandom() {
    }

    public IntRandom(int bound) {
        super(bound);
    }

    public IntRandom(int target, int maxTries, int bound) {
        super(Integer.valueOf(target), maxTries, Integer.valueOf(bound));
    }

    public IntRandom(int origin, int bound) {
        super(Integer.valueOf(origin), Integer.valueOf(bound));
    }

    public IntRandom(int target, int maxTries, int origin, int bound) {
        super(Integer.valueOf(target), maxTries, Integer.valueOf(origin), Integer.valueOf(bound));
    }

    public IntRandom(int target, int maxTries, int minTries, int origin, int bound) {
        super(target, maxTries, minTries, origin, bound);
    }

    @Override
    public Integer next() {
        return super.next();
    }

    @Override
    protected Integer getFromRandom0(Random random) {
        return random.nextInt();
    }

    @Override
    protected Integer getFromRandom(Random random, Integer bound) {
        return random.nextInt(bound);
    }

    @Override
    protected Integer getFromRandom(Random random, Integer origin, Integer bound) {
        return random.nextInt(origin, bound);
    }
}
