package org.vizzoid.utils.properties;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;
import java.util.function.Supplier;

public class Properties extends java.util.Properties {

    private static final String CONFIG_PATH = "src/config.properties";

    public Properties() {
        try {
            File file = new File(CONFIG_PATH);
            if (file.createNewFile()) System.out.println("Created Properties file");
            load(new FileReader(CONFIG_PATH));
        } catch (IOException e) {
            throw new RuntimeException();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                store(new FileWriter(CONFIG_PATH), " Auto editor settings");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public synchronized Object computeIfAbsent(Object key, Function<? super Object, ?> mappingFunction) {
        Object o;
        if (!containsKey(key)) {
            o = mappingFunction.apply(key);
            super.put(key, o);
        }
        else o = super.get(key);
        return o;
    }

    public String get(String key, Supplier<String> ifAbsent) {
        return (String) computeIfAbsent(key, o -> ifAbsent.get());
    }

    public int getInt(String key, Supplier<Integer> ifAbsent) {
        return Integer.parseInt(get(key, () -> String.valueOf(ifAbsent.get())));
    }

    public Color getColor(String key, Supplier<Color> ifAbsent) {
        return new Color(getInt(key, () -> ifAbsent.get().getRGB()));
    }

    public void set(String key, String value) {
        setProperty(key, value);
    }

    public void set(String key, int value) {
        set(key, String.valueOf(value));
    }

    public void set(String key, Color value) {
        set(key, value.getRGB());
    }

    public IntProperty getIntProperty(String key, Supplier<Integer> ifAbsent) {
        return new IntProperty(this, key, ifAbsent);
    }

    public StringProperty getStringProperty(String key, Supplier<String> ifAbsent) {
        return new StringProperty(this, key, ifAbsent);
    }

    public ColorProperty getColorProperty(String key, Supplier<Color> ifAbsent) {
        return new ColorProperty(this, key, ifAbsent);
    }

}
