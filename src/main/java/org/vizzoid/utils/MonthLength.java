package org.vizzoid.utils;

import java.time.LocalDate;
import java.time.Month;

/**
 * Object meant to represent and calculate the length of month(s) with context of month's differing lengths and February's leap day
 */
public class MonthLength {

    private static final int[] MONTH_LENGTHS = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int[] LEAP_MONTH_LENGTHS = new int[]{31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private int[] lengths;
    private int year;
    private int month = 1;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
        if (year % 4 == 0) lengths = LEAP_MONTH_LENGTHS;
        else lengths = MONTH_LENGTHS;
    }

    public MonthLength() {
        this(LocalDate.now().getYear());
        month = LocalDate.now().getMonthValue();
    }

    public MonthLength(int year) {
        lengths = year % 4 != 0 ? MONTH_LENGTHS : LEAP_MONTH_LENGTHS;
        this.year = year;
    }

    public void advance() {
        month++;
        if (month >= 13) {
            month = 1;
            setYear(year + 1);
        }
    }

    /**
     * Returns length name month from number (1-12)
     */
    public int of(int month) {
        return month <= 0 ? -1 : lengths[(month - 1) % 12];
    }

    public int of(Month month) {
        switch (month) {
            case JANUARY -> {
                return lengths[0];
            }
            case FEBRUARY -> {
                return lengths[1];
            }
            case MARCH -> {
                return lengths[2];
            }
            case APRIL -> {
                return lengths[3];
            }
            case MAY -> {
                return lengths[4];
            }
            case JUNE -> {
                return lengths[5];
            }
            case JULY -> {
                return lengths[6];
            }
            case AUGUST -> {
                return lengths[7];
            }
            case SEPTEMBER -> {
                return lengths[8];
            }
            case OCTOBER -> {
                return lengths[9];
            }
            case NOVEMBER -> {
                return lengths[10];
            }
            case DECEMBER -> {
                return lengths[11];
            }
            default -> throw new UnsupportedOperationException();
        }
    }

    public int next(int month) {
        return lengths[month != 12 ? month : 0];
    }

    public int next(Month month) {
        switch (month) {
            case JANUARY -> {
                return lengths[1];
            }
            case FEBRUARY -> {
                return lengths[2];
            }
            case MARCH -> {
                return lengths[3];
            }
            case APRIL -> {
                return lengths[4];
            }
            case MAY -> {
                return lengths[5];
            }
            case JUNE -> {
                return lengths[6];
            }
            case JULY -> {
                return lengths[7];
            }
            case AUGUST -> {
                return lengths[8];
            }
            case SEPTEMBER -> {
                return lengths[9];
            }
            case OCTOBER -> {
                return lengths[10];
            }
            case NOVEMBER -> {
                return lengths[11];
            }
            case DECEMBER -> {
                return lengths[0];
            }
            default -> throw new UnsupportedOperationException();
        }
    }

    public long mod(int months) {
        return mod(1, months);
    }

    public long mod(int startMonth, int months) {
        int days = 0;
        for (int i = 0; i < months; i++) {
            days += of(startMonth);
            startMonth++;
            if ((startMonth - 1) % 12 == 0) setYear(getYear() + 1);
        }
        return days;
    }

}
