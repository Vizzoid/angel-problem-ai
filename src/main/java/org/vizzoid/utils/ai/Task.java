package org.vizzoid.utils.ai;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface Task<T> {

    boolean canPerform(PriorityTask<T> holder);

    void perform(PriorityTask<T> holder);

    /**
     * @return if true, perform function will be called as many times as possible when it is being performed, otherwise it will be run once on activation and never again until reactivation
     */
    default boolean isRepeatable() {
        return false;
    }

    default boolean shouldStop(PriorityTask<T> holder) {
        return !isRepeatable() || !canPerform(holder);
    }

    static <T> SimpleTask<T> simple(Consumer<T> onPerform, Predicate<T> canPerform) {
        return new SimpleTask<>(onPerform, canPerform);
    }

    static <T> SimpleTask<T> simple(Consumer<T> onPerform, Predicate<T> canPerform, boolean repeatable) {
        return new SimpleTask<>(onPerform, canPerform, repeatable);
    }

}
