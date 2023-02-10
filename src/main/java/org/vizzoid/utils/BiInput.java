package org.vizzoid.utils;

/**
 * Input a string with two '%s' and it will apply and return
 */
public class BiInput<T, E> {
    private final String format;

    public BiInput(String format) {
        this.format = format;
    }

    public String apply(T t, E e) {
        return String.format(format, t, e);
    }

}
