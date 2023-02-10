package org.vizzoid.utils;

import org.apache.commons.text.WordUtils;

public class EnumUtils {
    /**
     * Formats an enum's name into readable context
     */
    public static String name(Enum<?> e) {
        return format(e.name());
    }

    public static String format(String name) {
        return WordUtils.capitalizeFully(name.replace("_", " "));
    }

    public static <T extends Enum<T>> T asEnum(String name, Class<T> clazz) {
        return Enum.valueOf(clazz, asEnum(name));

    }

    public static String asEnum(String name) {
        return name.toUpperCase().replaceAll(" ", "_");
    }

}
