package org.vizzoid.utils.properties;

import java.util.function.Supplier;

public class StringProperty extends AbstractProperty<String> {

    public StringProperty(Properties config, String key, Supplier<String> ifAbsent) {
        super(config, key, ifAbsent);
    }

    @Override
    public String get() {
        return config.get(key, ifAbsent);
    }

    @Override
    public void set(String s) {
        config.set(key, s);
    }
}
