package org.vizzoid.utils;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ExecutorTimerScheduler implements TimerScheduler {

    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> task;

    public ExecutorTimerScheduler() {

    }

    @Override
    public void delay(@NotNull Runnable runnable, long delay) {
        delay(runnable, delay * 50, TimeUnit.MILLISECONDS);
    }

    public void delay(@NotNull Runnable runnable, long delay, TimeUnit unit) {
        service.schedule(runnable, delay, unit);
    }

    @Override
    public void schedule(@NotNull Runnable onSecond) {
        schedule(onSecond, 1, 1, TimeUnit.SECONDS);
    }

    public void schedule(@NotNull Runnable runnable, long delay, long period, TimeUnit unit) {
        this.task = service.scheduleAtFixedRate(runnable, delay, period, unit);
    }

    @Override
    public void stop(@NotNull Runnable onStop, long delay) {
        stop(onStop, delay * 50, TimeUnit.MILLISECONDS);
    }

    public void stop(@NotNull Runnable runnable, long delay, TimeUnit unit) {
        service.schedule(runnable, delay, unit);
    }

    @Override
    public void cancel() {
        task.cancel(false);
    }
}
