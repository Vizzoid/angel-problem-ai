package org.vizzoid.utils;

import org.jetbrains.annotations.NotNull;

/**
 * Form of runnable that catches any throwable the runnable may throw and provides the context created at initialization
 */
public final class RunnableException {

    private final Runnable runnable;
    private final Throwable exception;

    public RunnableException(@NotNull Runnable runnable, Throwable context) {
        this.runnable = runnable;
        this.exception = context;
    }

    public void run() {
        try {
            runnable.run();
        } catch (Throwable context) {
            if (context.getCause() == null) context.initCause(exception).printStackTrace();
            else context.printStackTrace();
        }
    }

}
