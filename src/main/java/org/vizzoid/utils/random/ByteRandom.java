package org.vizzoid.utils.random;

import java.util.Random;

/**
 * Bounded Byte Array Random
 */
public class ByteRandom extends BoundedRandom<byte[]> {
    public ByteRandom(byte[] bytes) {
        super(bytes);
    }

    public ByteRandom(byte[] target, int maxTries, byte[] bytes) {
        super(target, maxTries, bytes);
    }

    public ByteRandom(byte[] target, int maxTries, int minTries, byte[] bytes) {
        super(target, maxTries, minTries, bytes);
    }

    @Override
    public byte[] next() {
        return super.next();
    }

    @Override
    protected byte[] getFromRandom0(Random random) {
        return new byte[0];
    }

    @Override
    protected byte[] getFromRandom(Random random, byte[] bytes) {
        random.nextBytes(bytes);
        return bytes;
    }

    @Override
    protected byte[] getFromRandom(Random random, byte[] origin, byte[] bytes) {
        return new byte[0];
    }
}
