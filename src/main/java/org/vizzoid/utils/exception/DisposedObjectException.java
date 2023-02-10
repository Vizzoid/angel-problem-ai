package org.vizzoid.utils.exception;

import org.vizzoid.utils.Disposable;

/**
 * Exception that occurs when disposed object is modified
 */
public class DisposedObjectException extends RaidServerRuntimeException {

    public DisposedObjectException(Disposable obj) {
        super("This object is disposed and cannot be modified!", obj.toString());
    }

}
