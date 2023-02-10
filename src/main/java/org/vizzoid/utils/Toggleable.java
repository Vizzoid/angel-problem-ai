package org.vizzoid.utils;

/**
 * Toggleable interface that allows an object to be opened and closed
 */
public interface Toggleable<T> extends Openable, Closeable<T> {

}
