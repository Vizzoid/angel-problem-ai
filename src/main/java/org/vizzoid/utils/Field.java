package org.vizzoid.utils;

/**
 * Represents reusable field object for applying private fields in an object
 */
public record Field<T, V>(java.lang.reflect.Field field) {

    public Field {
        field.setAccessible(true);
    }

    public void set(T instance, V newValue) {
        try {
            field.set(instance, newValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public V get(T instance) {
        try {
            //noinspection unchecked
            return (V) field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void set(V newValue) {
        set(null, newValue);
    }

    public V get() {
        return get(null);
    }

}
