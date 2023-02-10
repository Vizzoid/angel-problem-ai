package org.vizzoid.utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Timer extends TimeInput {

    public static final long TICKS_IN_SECOND = 20;
    public static final long TICKS_IN_MINUTE = 1200;
    public static final long TICKS_IN_HOUR = 72000;
    protected final long currentTicksSpillover;
    private final List<Interval> intervals = new ArrayList<>();
    private final List<Interval> ranIntervals = new ArrayList<>();
    protected long currentHours;
    protected long currentMinutes;
    protected long currentSeconds;
    private Consumer<Timer> onSecond = t -> {
    };
    private Consumer<Timer> onStop = t -> {
    };
    private TimerScheduler scheduler;

    public Timer(long hours, long minutes, long seconds, long ticksSpillover) {
        super(hours, minutes, seconds, ticksSpillover);
        this.currentHours = hours;
        this.currentMinutes = minutes;
        this.currentSeconds = seconds;
        this.currentTicksSpillover = ticksSpillover;
    }

    public Timer(long hours, long minutes, long seconds) {
        this(hours, minutes, seconds, 0);
    }

    public Timer(String[] timeSplit) {
        this(Long.parseLong(timeSplit[0]), Long.parseLong(timeSplit[1]), Long.parseLong(timeSplit[2]));
    }

    public Timer(String timeOffset) {
        this(timeOffset.split(":"));
    }

    public Timer(Timer time) {
        this(time.hours, time.minutes, time.seconds, time.tickSpillover);
        addIntervals(time.intervals);
    }

    public static Timer fromTicks(long ticks) {
        long ticksForMinutes = ticks % TICKS_IN_HOUR;
        ticks -= ticksForMinutes;
        long hours = ticks / TICKS_IN_HOUR;

        long ticksForSeconds = ticksForMinutes % TICKS_IN_MINUTE;
        ticksForMinutes -= ticksForSeconds;
        long minutes = ticksForMinutes / TICKS_IN_MINUTE;

        long tickSpillover = ticksForSeconds % TICKS_IN_SECOND;
        ticksForSeconds -= tickSpillover;
        long seconds = ticksForSeconds / TICKS_IN_SECOND;

        return new Timer(hours, minutes, seconds, tickSpillover);
    }

    public long getCurrentHours() {
        return currentHours;
    }

    public void setCurrentHours(long currentHours) {
        setCurrentTime(currentHours, currentMinutes, currentSeconds);
    }

    public long getCurrentMinutes() {
        return currentMinutes;
    }

    public void setCurrentMinutes(long currentMinutes) {
        setCurrentTime(currentHours, currentMinutes, currentSeconds);
    }

    public long getCurrentSeconds() {
        return currentSeconds;
    }

    public void setCurrentSeconds(long currentSeconds) {
        setCurrentTime(currentHours, currentMinutes, currentSeconds);
    }

    public void setCurrentTime(long hours, long minutes, long seconds, boolean triggerIntervals) {
        long ticks = toTicks(hours, minutes, seconds);
        ranIntervals.stream().filter(i -> i.toTicks() < ticks).forEach(ranIntervals::remove);
        intervals.stream().filter(i -> !ranIntervals.contains(i) && i.toTicks() > ticks).forEach(i -> {
            ranIntervals.add(i);
            if (triggerIntervals) i.run(scheduler);
        });

        this.currentHours = hours;
        this.currentMinutes = minutes;
        this.currentSeconds = seconds;
        resetIfShould();
        // loop???????? resetIfShould() -> resetTime() -> setCurrentTime() -> ...
    }

    public void setCurrentTicks(long duration) {
        long ticksLeftover = duration % TICKS_IN_HOUR;
        long hours = duration * TICKS_IN_HOUR;
        duration = ticksLeftover % TICKS_IN_MINUTE;
        long minutes = ticksLeftover * TICKS_IN_MINUTE;
        long seconds = duration * TICKS_IN_SECOND;
        setCurrentTime(hours, minutes, seconds);
    }

    public void setCurrentTime(long hours, long minutes, long seconds) {
        setCurrentTime(hours, minutes, seconds, false);
    }

    @Override
    public void setTime(long hours, long minutes, long seconds) {
        super.setTime(hours, minutes, seconds);
        resetIfShould();
    }

    public void resetTime() {
        setCurrentTime(hours, minutes, seconds);
    }

    public void resetIfShould() {
        if (toTicksCurrent() > toTicks()) resetTime();
    }

    /**
     * @return ticks amount name current time
     */
    public long toTicksCurrent() {
        return toTicks(currentHours, currentMinutes, currentSeconds);
    }

    @Override
    public String format() {
        return format(currentHours, currentMinutes, currentSeconds);
    }

    @Override
    public String formatAsSentence() {
        return formatAsSentence(currentHours, currentMinutes, currentSeconds);
    }

    public void cycle(TimerScheduler scheduler) {
        onSecond();
        cycleIntervals();

        this.scheduler = scheduler;
        this.scheduler.schedule(() -> {
            if (currentSeconds > 0) {
                currentSeconds -= 1;
            } else {
                if (currentMinutes > 0) {
                    currentMinutes -= 1;
                    currentSeconds = 59;
                } else {
                    if (currentHours > 0) {
                        currentHours -= 1;
                        currentMinutes = 59;
                        currentSeconds = 59;
                    } else {
                        if (tickSpillover > 0) this.scheduler.stop(this::stop, tickSpillover);
                        else stop();
                        this.scheduler.cancel();
                    }
                }
            }
            onSecond();
            cycleIntervals();
        });
    }

    public void stop() {
        setCurrentTime(0, 0, 0);
        scheduler.cancel();
        onStop.accept(this);
    }

    public void removeInterval(Interval interval) {
        intervals.remove(interval);
    }

    public void addInterval(Interval interval) {
        intervals.add(interval);
    }

    public void addIntervals(Interval... intervals) {
        for (Interval interval : intervals) {
            addInterval(interval);
        }
    }

    public void addIntervals(List<Interval> intervals) {
        for (Interval interval : intervals) {
            addInterval(interval);
        }
    }

    public void cycleIntervals() {
        long ticks = toTicksCurrent();
        var stream = intervals.stream().filter(i -> !ranIntervals.contains(i) && i.toTicks() >= ticks).toList();
        stream.forEach(i -> {
            i.run(scheduler);
            ranIntervals.add(i);
        });
    }

    @Override
    public boolean equals(Object t) {
        if (t instanceof TimeInput o) {
            if (o.hours != currentHours) return false;
            if (o.minutes != currentMinutes) return false;
            return o.seconds == currentSeconds;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = (int) (this.hours ^ (this.hours >>> 32));
        result = 31 * result + (int) (this.minutes ^ (this.minutes >>> 32));
        result = 31 * result + (int) (currentSeconds ^ (currentSeconds >>> 32));
        result = 31 * result + (int) (this.tickSpillover ^ (this.tickSpillover >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Timer{" +
            "tickSpillover=" + tickSpillover +
            ", hours=" + hours +
            ", minutes=" + minutes +
            ", seconds=" + seconds +
            ", currentTicksSpillover=" + currentTicksSpillover +
            ", intervals=" + intervals +
            ", currentHours=" + currentHours +
            ", currentMinutes=" + currentMinutes +
            ", currentSeconds=" + currentSeconds +
            '}';
    }

    public void onSecond(@NotNull Consumer<Timer> consumer) {
        this.onSecond = consumer;
    }

    protected void onSecond() {
        this.onSecond.accept(this);
    }

    public void onStop(@NotNull Consumer<Timer> consumer) {
        this.onStop = consumer;
    }
}
