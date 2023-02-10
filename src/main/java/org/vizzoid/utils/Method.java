package org.vizzoid.utils;

import java.lang.reflect.InvocationTargetException;

/**
 * Represents reusable method object for applying private functions in an object
 */
public record Method<T, K>(java.lang.reflect.Method method) {

    public Method {
        method.setAccessible(true);
    }

    public K invoke(T instance, Object... inputs) {
        try {
            //noinspection unchecked
            return (K) method.invoke(instance, inputs);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }
    }

}
