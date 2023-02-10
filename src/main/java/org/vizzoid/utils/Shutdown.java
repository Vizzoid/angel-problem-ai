package org.vizzoid.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Shutdown hook that allows priority and hooks. The hooks run on priority, which means that the hooks will run on descending priority allowing for tasks that depend on other tasks to fire first,
 * and independent tasks to run last.
 * The hooks allow throwable catching that provides with easy debugging without ending the shutdown hook
 */
public class Shutdown {

    private static final MultiMap<Priority, Hook> hooks = new MultiMap<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (Priority priority : Priority.values()) {
                List<Hook> list = hooks.get(priority);
                if (list == null) continue;

                for (Hook hook : list) {
                    try {
                        hook.run();
                    } catch (Throwable t) {
                        if (t.getCause() == null) t.initCause(hook.getContext()).printStackTrace();
                        else t.printStackTrace();
                    }
                }
            }
        }));
    }

    public static void add(Hook hook, Priority priority) {
        hooks.add(priority, hook);
    }

    public static void add(Hook hook) {
        add(hook, Priority.NORMAL);
    }

    public static void add(ThrowableRunnable hook, Priority priority) {
        add(new Hook(hook, new RuntimeException()), priority);
    }

    public static void add(ThrowableRunnable hook) {
        add(new Hook(hook, new RuntimeException()));
    }

    public static List<Hook> get(Priority priority) {
        return new ArrayList<>(hooks.get(priority));
    }

    public static MultiMap<Priority, Hook> getAll() {
        return new MultiMap<>(hooks);
    }

    public static Map<Hook, Priority> reverse() {
        return hooks.reverse();
    }

}
