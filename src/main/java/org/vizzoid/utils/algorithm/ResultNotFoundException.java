package org.vizzoid.utils.algorithm;

import java.io.Serial;

public class ResultNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -2626614614190362799L;

    public ResultNotFoundException(Object target, Object collection) {
        super("Could not find (" + target + ") in (" + collection + ")");
    }
}
