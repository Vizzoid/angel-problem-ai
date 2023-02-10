package org.vizzoid.utils;

/**
 * Object that takes input and surrounds it in prefix and suffixes. Useful for urls, should be stored in static final field.
 */
public class Input<T> {
    private final String prefix;
    private final String suffix;

    public Input(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public Input(String prefix) {
        this(prefix, "");
    }

    public String apply(T input) {
        return prefix + input + suffix;
    }
}
