package org.vizzoid.utils;

import org.jetbrains.annotations.NotNull;

public interface TimerScheduler {

    static ExecutorTimerScheduler executor() {
        return new ExecutorTimerScheduler();
    }

    void delay(@NotNull Runnable runnable, long delay);

    void schedule(@NotNull Runnable onSecond);

    void stop(@NotNull Runnable onStop, long delay);

    void cancel();

}
