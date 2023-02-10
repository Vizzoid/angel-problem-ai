package org.vizzoid.utils.exception;

import org.vizzoid.utils.Invalidatable;

/**
 * Represents error that is thrown when an invalidated object is attempted to be modified
 */
public class InvalidatedObjectException extends RaidServerRuntimeException {

    public InvalidatedObjectException(Invalidatable obj) {
        super("This object is invalidated and cannot be modified!", obj.toString());
    }

}
