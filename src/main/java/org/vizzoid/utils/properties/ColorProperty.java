package org.vizzoid.utils.properties;

import java.awt.*;
import java.util.function.Supplier;

public class ColorProperty extends AbstractProperty<Color> {

    public ColorProperty(Properties config, String key, Supplier<Color> ifAbsent) {
        super(config, key, ifAbsent);
    }

    @Override
    public Color get() {
        return config.getColor(key, ifAbsent);
    }

    @Override
    public void set(Color color) {
        config.set(key, color);
    }
}
