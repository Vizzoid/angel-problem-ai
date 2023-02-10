package org.vizzoid.utils;

/**
 * Represents wrapper around object to pass it as T type while not creating a new instance name it
 * Used for convenience but SHOULDN'T be used in Bukkit logic.
 * Often Bukkit logic assumes that its interface is implemented by the Craft version,
 * and doesn't support, will throw errors, try to cast, or uses methods name that class,
 * meaning that using this as implementation in Bukkit logic will always throw a ClassCastException
 * Instead, this should only be used in RaidServer logic, and NOT for NMS operations.
 */
public abstract class Container<T> implements org.vizzoid.utils.data.Container<T> {

    private T storage;

    public Container(T storage) {
        this.storage = storage;
    }

    public T getStorage() {
        return storage;
    }

    public void setStorage(T storage) {
        this.storage = storage;
    }
}
