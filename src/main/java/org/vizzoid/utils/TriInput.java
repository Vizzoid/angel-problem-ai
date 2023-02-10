package org.vizzoid.utils;

/**
 * Input a string with three '%s' and it will apply and return
 */
public class TriInput<E, E1, E2> {
    private final String format;

    public TriInput(String format) {
        this.format = format;
    }

    public String apply(E e, E1 e1, E2 e2) {
        return String.format(format, e, e1, e2);
    }

}
