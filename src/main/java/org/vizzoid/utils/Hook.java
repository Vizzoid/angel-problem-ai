package org.vizzoid.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Hook that provides a runnable that can throw an exception, context, and the ability to combine other runnables so that they can happen before or after the original one
 */
public class Hook {

    private final ThrowableRunnable run;
    private final RuntimeException context;
    private @Nullable List<ThrowableRunnable> pre;
    private @Nullable List<ThrowableRunnable> suf;

    public Hook(ThrowableRunnable run, RuntimeException context) {
        this.run = run;
        this.context = context;
    }

    public RuntimeException getContext() {
        return context;
    }

    public void run() throws Throwable {
        if (pre != null) {
            for (ThrowableRunnable exceptionRunnable : pre) {
                exceptionRunnable.run();
            }
        }
        run.run();
        if (suf != null) {
            for (ThrowableRunnable exceptionRunnable : suf) {
                exceptionRunnable.run();
            }
        }
    }

    private @NotNull List<ThrowableRunnable> pre() {
        return pre != null ? pre : (pre = new ArrayList<>());
    }

    private @NotNull List<ThrowableRunnable> suf() {
        return suf != null ? suf : (suf = new ArrayList<>());
    }

    /**
     * Adds runnable to run before initial runnable
     * Last runnable added will run first
     */
    public void after(ThrowableRunnable runnable) {
        pre().add(0, runnable);
    }

    /**
     * Adds runnable to run after initial runnable
     * Last runnable added will run last
     */
    public void andThen(ThrowableRunnable runnable) {
        suf().add(runnable);
    }

}
