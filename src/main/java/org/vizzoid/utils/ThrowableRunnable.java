package org.vizzoid.utils;

/**
 * Runnable that can throw an exception
 */
@FunctionalInterface
public interface ThrowableRunnable {

    void run() throws Throwable;

}
