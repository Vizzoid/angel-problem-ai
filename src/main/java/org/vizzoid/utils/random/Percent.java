package org.vizzoid.utils.random;

import org.jetbrains.annotations.Range;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents percent of an event
 */
public class Percent {

    private final int value;
    private final int bound;

    private Percent(int value, int bound) {
        this.value = value;
        this.bound = bound;
    }

    public int getValue() {
        return value;
    }

    public int getBound() {
        return bound;
    }

    public boolean passed() {
        return (ThreadLocalRandom.current().nextInt(bound) + 1) <= value;
    }

    public static Percent always() {
        return new Always();
    }

    public static Percent never() {
        return new Never();
    }

    public static Percent of10(@Range(from = 0, to = 10) int value) {
        return of(value, 10);
    }

    public static Percent of100(@Range(from = 0, to = 100) int value) {
        return of(value, 100);
    }

    public static Percent of1000(@Range(from = 0, to = 1000) int value) {
        return of(value, 1000);
    }

    public static Percent of10000(@Range(from = 0, to = 10000) int value) {
        return of(value, 10000);
    }

    public static Percent of100000(@Range(from = 0, to = 100000) int value) {
        return of(value, 100000);
    }

    public static Percent of1000000(@Range(from = 0, to = 1000000) int value) {
        return of(value, 1000000);
    }

    public static Percent of(int value, int bound) {
        if (value == 0) return never();
        if (value == bound) return always();
        return new Percent(value, bound);
    }

    public static class Always extends Percent {
        public Always() {
            super(100, 100);
        }

        @Override
        public boolean passed() {
            return true;
        }
    }

    public static class Never extends Percent {
        public Never() {
            super(100, 100);
        }

        @Override
        public boolean passed() {
            return false;
        }
    }

}
