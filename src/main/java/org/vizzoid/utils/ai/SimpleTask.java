package org.vizzoid.utils.ai;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class SimpleTask<T> implements Task<T> {

    private final Consumer<T> onPerform;
    private final Predicate<T> canPerform;
    private final boolean repeatable;

    public SimpleTask(Consumer<T> onPerform, Predicate<T> canPerform) {
        this(onPerform, canPerform, false);
    }

    public SimpleTask(Consumer<T> onPerform, Predicate<T> canPerform, boolean repeatable) {
        this.onPerform = onPerform;
        this.canPerform = canPerform;
        this.repeatable = repeatable;
    }

    @Override
    public boolean canPerform(PriorityTask<T> holder) {
        return canPerform.test(holder.holder);
    }

    @Override
    public void perform(PriorityTask<T> holder) {
        onPerform.accept(holder.holder);
    }

    @Override
    public boolean isRepeatable() {
        return repeatable;
    }
}
