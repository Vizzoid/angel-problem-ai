package org.vizzoid.utils;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Class that represents a milestone for an object or event with a date and time
 */
public class Milestone<T> {

    private final @NotNull T t;
    private final TimeStamp then;

    public Milestone(@NotNull T t, TimeStamp then) {
        this.t = t;
        this.then = then;
    }

    public Milestone(@NotNull T t, long month, long dayOfMonth, long year, long hour, long minute, long seconds, long tickSpillover) {
        this(t, new TimeStamp(month, dayOfMonth, year, hour, minute, seconds, tickSpillover));
    }

    public Milestone(@NotNull T t, long month, long dayOfMonth, long year, long hour, long minute, long seconds) {
        this(t, new TimeStamp(month, dayOfMonth, year, hour, minute, seconds));
    }

    public Milestone(@NotNull T t, LocalDate date, long hour, long minute, long seconds, long tickSpillover) {
        this(t, new TimeStamp(date, hour, minute, seconds, tickSpillover));
    }

    public Milestone(@NotNull T t, LocalDate date, long hour, long minute, long seconds) {
        this(t, new TimeStamp(date, hour, minute, seconds));
    }

    public Milestone(@NotNull T t, LocalDateTime l) {
        this(t, l.toLocalDate(), l.getHour(), l.getMinute(), l.getSecond());
    }

    public static <T> Milestone<T> now(@NotNull T t) {
        return new Milestone<>(t, TimeStamp.now());
    }

    public @NotNull T get() {
        return t;
    }

    public @NotNull Map<String, Object> serialize() {
        return new DataMap("get", t, "then", then.toString());
    }

}
