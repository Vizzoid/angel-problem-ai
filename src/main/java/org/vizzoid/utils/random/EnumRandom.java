package org.vizzoid.utils.random;

import java.util.Random;

public class EnumRandom<E extends Enum<E>> extends BoundedRandom<Enum<E>> {

    private final E[] values;

    public EnumRandom(Class<E> enumClazz) {
        this.values = enumClazz.getEnumConstants();
    }

    public EnumRandom(Enum<E> target, int maxTries, Class<E> enumClazz) {
        super(target, maxTries);
        this.values = enumClazz.getEnumConstants();
    }

    public EnumRandom(Enum<E> target, int maxTries, int minTries, Class<E> enumClazz) {
        super(target, maxTries, minTries);
        this.values = enumClazz.getEnumConstants();
    }

    public EnumRandom(Enum<E> bound, Class<E> enumClazz) {
        super(bound);
        this.values = enumClazz.getEnumConstants();
    }

    public EnumRandom(Enum<E> target, int maxTries, Enum<E> bound, Class<E> enumClazz) {
        super(target, maxTries, bound);
        this.values = enumClazz.getEnumConstants();
    }

    public EnumRandom(Enum<E> target, int maxTries, int minTries, Enum<E> bound, Class<E> enumClazz) {
        super(target, maxTries, minTries, bound);
        this.values = enumClazz.getEnumConstants();
    }

    public EnumRandom(Enum<E> origin, Enum<E> bound, Class<E> enumClazz) {
        super(origin, bound);
        this.values = enumClazz.getEnumConstants();
    }

    public EnumRandom(Enum<E> target, int maxTries, Enum<E> origin, Enum<E> bound, Class<E> enumClazz) {
        super(target, maxTries, origin, bound);
        this.values = enumClazz.getEnumConstants();
    }

    public EnumRandom(Enum<E> target, int maxTries, int minTries, Enum<E> origin, Enum<E> bound, Class<E> enumClazz) {
        super(target, maxTries, minTries, origin, bound);
        this.values = enumClazz.getEnumConstants();
    }

    @Override
    protected Enum<E> getFromRandom0(Random random) {
        return values[random.nextInt(values.length)];
    }

    /**
     * @param bound inclusive
     */
    @Override
    protected Enum<E> getFromRandom(Random random, Enum<E> bound) {
        return values[random.nextInt(bound.ordinal() + 1)];
    }

    /**
     * @param bound inclusive
     */
    @Override
    protected Enum<E> getFromRandom(Random random, Enum<E> origin, Enum<E> bound) {
        return values[random.nextInt(origin.ordinal(), bound.ordinal() + 1)];
    }
}
