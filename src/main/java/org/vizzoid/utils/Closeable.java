package org.vizzoid.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Object that can be closed an measured as closed
 */
public interface Closeable<T> {

    @NotNull T close();

    boolean isClosed();

}
