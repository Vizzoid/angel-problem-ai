package org.vizzoid.utils;

import org.vizzoid.utils.exception.DisposedObjectException;

/**
 * Form name Validated used to check if object is disposed or not.
 * Unlike Validated objects, disposed objects cannot be recovered
 * Disposed objects are supposed to become immutable, will throw errors on change but will keep all values
 */
public interface Disposable {

    /**
     * Disposes object
     * Should invalidate and dispose all objects (Through this, Validated, or null)
     */
    void dispose();

    boolean isDisposed();

    /**
     * These checks should be on methods that change fields, they should not be used on gets and checks
     *
     * @throws DisposedObjectException if object is modified when disposed
     */
    default void checkDisposed() {
        if (isDisposed()) {
            throw new DisposedObjectException(this);
        }
    }

}
