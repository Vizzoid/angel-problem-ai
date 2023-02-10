package org.vizzoid.utils.ai;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Brain<T> {

    private final T holder;

    public Brain(T holder) {
        this.holder = holder;
    }

    public Brain<T> cycle() {
        cycleGoals();
        return this;
    }

    // goal selector

    private final List<PriorityTask<T>> tasks = new ArrayList<>();
    PriorityTask<T> performing = null;

    private void cycleGoals() {
        if (performing != null) {
            if (performing.isRepeatable()) {
                performing.perform();
                performing.cyclesSincePerform = 0;
            }
            if (performing.shouldStop()) {
                performing.performing = false;
                performing = null;
            }
            else return;
        }

        for (PriorityTask<T> task : tasks) {
            if (!task.canPerform()) {
                task.cyclesSincePerform++;
                continue;
            }
            performTask(task);
            return;
        }
    }

    public void performTask(PriorityTask<T> task) {
        task.perform();
        task.cyclesSincePerform = 0;
        performing = task;
        task.performing = true;
    }

    public Brain<T> addTask(int priority, Task<T> task) {
        return this.addTask(new PriorityTask<>(task, priority, this, holder));
    }

    private Brain<T> addTask(PriorityTask<T> task) {
        tasks.add(task);
        tasks.sort(Comparator.comparingInt(p -> p.priority));
        return this;
    }

    public void removeTask(Task<T> task) {
        tasks.removeIf(tPriorityTask -> tPriorityTask.task == task);
    }

    public void removeTask(PriorityTask<T> task) {
        tasks.remove(task);
    }

    public @Nullable PriorityTask<T> getPerformingTask() {
        return performing;
    }

    public boolean isPerformingTask() {
        return performing != null;
    }

    // goal selector end



}
