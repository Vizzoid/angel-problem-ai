package org.vizzoid.utils;

public record BooleanField<T>(java.lang.reflect.Field field) {

    public BooleanField {
        field.setAccessible(true);
    }

    public void set(T instance, boolean newValue) {
        try {
            field.set(instance, newValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean get(T instance) {
        try {
            return Boolean.TRUE.equals(field.get(instance));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

}
