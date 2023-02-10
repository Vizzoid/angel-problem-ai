package org.vizzoid.utils;

public class Interval extends TimeInput {

    private final Runnable run;

    public Interval(long hours, long minutes, long seconds, long ticksSpillover, Runnable run) {
        super(hours, minutes, seconds, ticksSpillover);
        this.run = run;
    }

    public Interval(long hours, long minutes, long seconds, Runnable run) {
        super(hours, minutes, seconds);
        this.run = run;
    }

    public Interval(String[] timeSplit, Runnable run) {
        super(timeSplit);
        this.run = run;
    }

    public Interval(String timeOffset, Runnable run) {
        super(timeOffset);
        this.run = run;
    }

    // semantics
    public static Interval of(long hours, long minutes, long seconds, long ticksSpillover, Runnable run) {
        return new Interval(hours, minutes, seconds, ticksSpillover, run);
    }

    public static Interval of(long hours, long minutes, long seconds, Runnable run) {
        return new Interval(hours, minutes, seconds, run);
    }

    /**
     * @return Interval name time's current value
     */
    public static Interval current(Timer time, Runnable run) {
        return new Interval(time.currentHours, time.currentMinutes, time.currentSeconds, time.currentTicksSpillover, run);
    }

    /**
     * @return Interval name time's original value
     */
    public static Interval original(TimeInput time, Runnable run) {
        return new Interval(time.hours, time.minutes, time.seconds, time.tickSpillover, run);
    }

    /**
     * @param percentOfTime percent value 1.00-0.00
     * @return Interval that triggers after percent name time has passed (60 sec, 0.1, after 6
     */
    public static Interval percent(TimeInput time, float percentOfTime, Runnable run) {
        double hoursD = (time.hours * percentOfTime);
        long hours = (long) hoursD;
        double minutesD = (time.minutes * percentOfTime) + ((hoursD - hours) * 60);
        long minutes = (long) minutesD;
        double secondsD = (time.seconds * percentOfTime) + ((minutesD - minutes) * 60);
        long seconds = (long) secondsD;
        long tickSpillover = (long) ((long) (time.tickSpillover * percentOfTime) + ((secondsD - seconds) * 20));
        return new Interval(hours, minutes, seconds, tickSpillover, run);
    }

    public static Interval thirds(TimeInput time, int numerator, Runnable run) {
        switch (numerator) {
            case 0 -> {
                return percent(time, 0f, run);
            }
            case 1 -> {
                return percent(time, 0.333333333f, run);
            }
            case 2 -> {
                return percent(time, 0.666666666f, run);
            }
            case 3 -> {
                return percent(time, 1f, run);
            }
            default -> throw new AssertionError();
        }
    }

    public static Interval ninths(TimeInput time, int numerator, Runnable run) {
        switch (numerator) {
            case 0 -> {
                return percent(time, 0f, run);
            }
            case 1 -> {
                return percent(time, 0.111111111f, run);
            }
            case 2 -> {
                return percent(time, 0.222222222f, run);
            }
            case 3 -> {
                return percent(time, 0.333333333f, run);
            }
            case 4 -> {
                return percent(time, 0.444444444f, run);
            }
            case 5 -> {
                return percent(time, 0.555555556f, run);
            }
            case 6 -> {
                return percent(time, 0.666666667f, run);
            }
            case 7 -> {
                return percent(time, 0.777777778f, run);
            }
            case 8 -> {
                return percent(time, 0.888888889f, run);
            }
            case 9 -> {
                return percent(time, 1f, run);
            }
            default -> throw new AssertionError();
        }
    }

    public static Interval start(TimeInput time, Runnable run) {
        return original(time, run);
    }

    public static Interval end(Runnable run) {
        return new Interval(0, 0, 0, 0, run);
    }

    public void run(TimerScheduler scheduler) {
        if (tickSpillover > 0) scheduler.delay(run, tickSpillover);
        else run.run();
    }

    @Override
    public boolean equals(Object t) {
        if (t instanceof Timer o) {
            if (o.currentHours != hours) return false;
            if (o.currentMinutes != minutes) return false;
            return o.currentSeconds == seconds;
        } else if (t instanceof Interval o) {
            if (o.hours != hours) return false;
            if (o.minutes != minutes) return false;
            return o.seconds == seconds;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Interval{" +
            "tickSpillover=" + tickSpillover +
            ", hours=" + hours +
            ", minutes=" + minutes +
            ", seconds=" + seconds +
            '}';
    }
}
