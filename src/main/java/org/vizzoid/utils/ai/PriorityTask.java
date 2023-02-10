package org.vizzoid.utils.ai;

// union between brain and task -- contains useful information and vital data for tasks
// DO NOT STORE SOMEWHERE
public final class PriorityTask<T> {

    public final Task<T> task;
    public final int priority;
    public final Brain<T> brain;
    public final T holder;
    public int cyclesSincePerform = 0;
    public boolean performing = false;

    public PriorityTask(Task<T> task, int priority, Brain<T> brain, T holder) {
        this.task = task;
        this.priority = priority;
        this.brain = brain;
        this.holder = holder;
    }

    public void setPriority(int priority) {
        brain.removeTask(this);
        PriorityTask<T> task = new PriorityTask<>(this.task, priority, brain, holder);
        if (brain.performing == this) brain.performing = task;
        task.cyclesSincePerform = cyclesSincePerform;
        task.performing = performing;
    }

    public void perform() {
        task.perform(this);
    }

    public boolean canPerform() {
        return task.canPerform(this);
    }

    public boolean shouldStop() {
        return task.shouldStop(this);
    }

    public boolean isRepeatable() {
        return task.isRepeatable();
    }

}
