package org.vizzoid.utils.properties;

import java.util.function.Supplier;

public abstract class AbstractProperty<T> implements Property<T> {

    protected final Properties config;
    protected final String key;
    protected final Supplier<T> ifAbsent;

    public AbstractProperty(Properties config, String key, Supplier<T> ifAbsent) {
        this.config = config;
        this.key = key;
        this.ifAbsent = ifAbsent;

        if (!config.containsKey(key)) set(ifAbsent.get());
    }

    @Override
    public abstract T get();

    @Override
    public abstract void set(T t);

    @Override
    public String toString() {
        return String.valueOf(get());
    }
}
