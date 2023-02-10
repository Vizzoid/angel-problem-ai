package org.vizzoid.utils;

import org.vizzoid.utils.exception.InvalidatedObjectException;

/**
 * Represents a class that can be invalidated and validated.
 * Validated objects are usually used to make sure an object another object copies does not affect the new one
 */
public interface Invalidatable {

    /**
     * Validates object
     */
    void validate();

    /**
     * Invalidates object
     */
    void invalidate();

    /**
     * @return if object is valid
     */
    boolean isValid();

    /**
     * These checks should be on methods that change fields, they should not be used on gets and checks
     *
     * @throws InvalidatedObjectException if object is modified when invalid
     */
    default void checkValidity() {
        if (!isValid()) {
            throw new InvalidatedObjectException(this);
        }
    }

}
