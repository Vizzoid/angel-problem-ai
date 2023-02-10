package org.vizzoid.utils;

/**
 * Closeable that is purely void, meaning it does not require any return
 */
public interface CloseableVoid {

    void close();

    boolean isClosed();

}
