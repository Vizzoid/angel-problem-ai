package org.vizzoid.utils.properties;

import java.util.function.Supplier;

public class IntProperty extends Number implements Property<Integer> {

    private final Properties config;
    private final String key;
    private final Supplier<Integer> ifAbsent;

    public IntProperty(Properties config, String key, Supplier<Integer> ifAbsent) {
        this.config = config;
        this.key = key;
        this.ifAbsent = ifAbsent;

        if (!config.containsKey(key)) set(ifAbsent.get());
    }

    @Override
    public Integer get() {
        return config.getInt(key, ifAbsent);
    }

    @Override
    public void set(Integer integer) {
        config.set(key, integer);
    }

    @Override
    public int intValue() {
        return get();
    }

    @Override
    public long longValue() {
        return (long) get();
    }

    @Override
    public float floatValue() {
        return (float) get();
    }

    @Override
    public double doubleValue() {
        return (double) get();
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }
}
