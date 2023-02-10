package org.vizzoid.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class TimeStamp extends TimeInput {

    public static final long TICKS_IN_SECOND = 20;
    public static final long TICKS_IN_MINUTE = 1200;
    public static final long TICKS_IN_HOUR = 72000;
    public static final long TICKS_IN_DAY = 1728000;
    public static final long AVERAGE_TICKS_IN_MONTH = 51840000;
    public static final long AVERAGE_TICKS_IN_YEAR = 622080000;

    private static final TimeStamp EMPTY = new TimeStamp(0, 0, 0, 0, 0, 0, 0);
    private static final String SPLIT = ":";
    private static final String DATE_SPLIT = "/";
    private final Formatter formatter;
    protected long year;
    protected long month;
    protected long dayOfMonth;
    protected long totalTicks;

    protected TimeStamp(long month, long dayOfMonth, long year, long hour, long minute, long seconds, long tickSpillover, boolean loadTotalTicks) {
        super(0);
        this.tickSpillover = tickSpillover;
        this.seconds = seconds;
        this.minutes = minute;
        this.hours = hour;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.year = year;

        adjust(loadTotalTicks);

        formatter = new Formatter(); // NEEDS TO BE LAST STATEMENT
        // USES FIELDS IN INITIALIZER // not anymore
    }

    private void adjust(boolean loadTotalTicks) {
        // Formats time so, for example, months dont go over 12, days over 31, etc
        int length;
        if (!loadTotalTicks) tickSpillover = totalTicks;

        {
            long seconds = ticksToSeconds(tickSpillover);
            tickSpillover -= secondsToTicks(seconds);
            this.seconds += seconds;
        }
        {
            long minutes = secondsToMinutes(seconds);
            seconds -= minutesToSeconds(minutes);
            this.minutes += minutes;
        }
        {
            long hours = minutesToHours(minutes);
            minutes -= hoursToMinutes(hours);
            this.hours += hours;
        }
        {
            long dayOfMonth = hoursToDays(hours);
            hours -= daysToHours(dayOfMonth);
            this.dayOfMonth += dayOfMonth;
        }
        {
            long month = daysToMonths(dayOfMonth);
            dayOfMonth -= monthsToDays(month);
            this.month += month;
        }
        {
            long year = monthsToYears(month);
            month -= yearsToMonths(year);
            this.year += year;
        }

        if (loadTotalTicks) setTotalTicks();
    }

    public TimeStamp(long ticks) {
        this(0, 0, 0, 0, 0, 0, ticks, true);
    }

    public TimeStamp(long month, long dayOfMonth, long year, long hour, long minute, long seconds, long tickSpillover) {
        this(month, dayOfMonth, year, hour, minute, seconds, tickSpillover, true);
    }

    public TimeStamp(long month, long dayOfMonth, long year, long hour, long minute, long seconds) {
        this(month, dayOfMonth, year, hour, minute, seconds, 0);
    }

    public TimeStamp(LocalDate date, long hour, long minute, long seconds, long tickSpillover) {
        this(date.getMonthValue(), date.getDayOfMonth(), date.getYear(), hour, minute, seconds, tickSpillover);
    }

    public TimeStamp(LocalDate date, long hour, long minute, long seconds) {
        this(date, hour, minute, seconds, 0);
    }

    public TimeStamp(long hour, long minute, long seconds) {
        this(hour, minute, seconds, 0);
    }

    public TimeStamp(long hour, long minute, long seconds, long tickSpillover) {
        this(0, 0, 0, hour, minute, seconds, tickSpillover);
    }

    public TimeStamp(LocalDateTime l) {
        this(l.getMonthValue(), l.getDayOfMonth(), l.getYear(), l.getHour(), l.getMinute(), l.getSecond(), 0);
    }

    public TimeStamp(LocalDate date) {
        this(date, 0, 0, 0);
    }

    @Override
    public void setTime(long hours, long minutes, long seconds) {

    }

    public static TimeStamp parse(String s) {
        return s != null ? empty().getFormatter().parse(s) : null;
    }

    public static TimeStamp empty() {
        return EMPTY;
    }

    public static TimeStamp now() {
        if (Thread.currentThread() instanceof TestThread thread) return thread.now;
        TimeStamp stamp = new TimeStamp(LocalDateTime.now());
        stamp.totalTicks = Calendar.getInstance().getTimeInMillis();
        return stamp;
    }

    public static TimeStamp deserialize(Map<String, Object> args) {
        return args != null ? parse((String) args.get("time")) : null;
    }

    public long getYear() {
        return year;
    }

    public long getMonth() {
        return month;
    }

    public long getDayOfMonth() {
        return dayOfMonth;
    }

    public LocalDate date() {
        return LocalDate.of((int) year, (int) month, (int) dayOfMonth);
    }

    public LocalTime time() {
        return LocalTime.of((int) hours, (int) minutes, (int) seconds);
    }

    public LocalDateTime dateTime() {
        return LocalDateTime.of((int) year, (int) month, (int) dayOfMonth, (int) hours, (int) minutes, (int) seconds);
    }

    public static String toString(TimeStamp timeStamp) {
        return timeStamp != null ? timeStamp.toString() : null;
    }

    /**
     * Returns null if absent instead of empty time stamp
     */
    public static TimeStamp parseWithNull(String string) {
        TimeStamp timeStamp = parse(string);
        return EMPTY.equals(timeStamp) ? null : timeStamp;
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public boolean isEmpty() {
        return equals(empty());
    }

    @Override
    public String toString() {
        return "TimeStamp{" +
            "totalTicks=" + totalTicks + ", " +
            "format=" + getFormatter().formatWithSpillover() +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TimeStamp timeStamp)) return false;

        return totalTicks == timeStamp.totalTicks;
    }

    public static TimeStamp of(long totalTicks) {
        if (totalTicks < 0) return null;
        return new TimeStamp(totalTicks);
    }

    @Override
    public int hashCode() {
        int result = (int) (getYear() ^ (getYear() >>> 32));
        result = 31 * result + (int) (getMonth() ^ (getMonth() >>> 32));
        result = 31 * result + (int) (getDayOfMonth() ^ (getDayOfMonth() >>> 32));
        result = 31 * result + getFormatter().hashCode();
        return result;
    }

    public boolean isAfter(TimeStamp stamp) {
        return toTicks() > stamp.toTicks();
    }

    public boolean isAfter(LocalDate stamp) {
        return toTicks() > new TimeStamp(stamp).toTicks();
    }

    public boolean isAfter(LocalDateTime stamp) {
        return toTicks() > new TimeStamp(stamp).toTicks();
    }

    public boolean isBefore(TimeStamp stamp) {
        return toTicks() < stamp.toTicks();
    }

    public boolean isBefore(LocalDate stamp) {
        return toTicks() < new TimeStamp(stamp).toTicks();
    }

    public boolean isBefore(LocalDateTime stamp) {
        return toTicks() < new TimeStamp(stamp).toTicks();
    }

    public boolean isSameDay(TimeStamp stamp) {
        return getYear() == stamp.getYear() &&
            getMonth() == stamp.getMonth() &&
            getDayOfMonth() == stamp.getDayOfMonth();
    }

    public boolean isSameDay(LocalDate date) {
        return getYear() == date.getYear() &&
            getMonth() == date.getMonthValue() &&
            getDayOfMonth() == date.getDayOfMonth();
    }

    public boolean isSameDay(LocalDateTime date) {
        return getYear() == date.getYear() &&
            getMonth() == date.getMonthValue() &&
            getDayOfMonth() == date.getDayOfMonth();
    }

    public TimeStamp plusYears(long years) {
        return new TimeStamp(month, dayOfMonth, year + years, hours, minutes, seconds, tickSpillover);
    }

    public TimeStamp plusMonths(long months) {
        return new TimeStamp(month + months, dayOfMonth, year, hours, minutes, seconds, tickSpillover);
    }

    public TimeStamp plusDays(long days) {
        return new TimeStamp(month, dayOfMonth + days, year, hours, minutes, seconds, tickSpillover);
    }

    public TimeStamp plusHours(long hour) {
        return new TimeStamp(month, dayOfMonth, year, hours + hour, minutes, seconds, tickSpillover);
    }

    public TimeStamp plusMinutes(long minute) {
        return new TimeStamp(month, dayOfMonth, year, hours, minutes + minute, seconds, tickSpillover);
    }

    public TimeStamp plusSeconds(long second) {
        return new TimeStamp(month, dayOfMonth, year, hours, minutes, seconds + second, tickSpillover);
    }

    public TimeStamp plusTicks(long ticks) {
        return new TimeStamp(month, dayOfMonth, year, hours, minutes, seconds, tickSpillover + ticks);
    }

    public TimeStamp nextYear() {
        return plusYears(1);
    }

    public TimeStamp nextMonth() {
        return plusMonths(1);
    }

    public TimeStamp nextDay() {
        return plusDays(1);
    }

    public TimeStamp nextHour() {
        return plusHours(1);
    }

    public TimeStamp nextMinute() {
        return plusMinutes(1);
    }

    public TimeStamp nextSecond() {
        return plusSeconds(1);
    }

    public TimeStamp nextTick() {
        return plusTicks(1);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();

        map.put("time", getFormatter().format());

        return map;
    }

    /**
     * Returns the tick duration from this timestamp to the inputted one. If number is negative, the input is before this timestamp
     * Returns duration of now until input. If duration < 0, then the input is before this, if duration > 0 the input is after this. If the duration == 0 the input is within one tick before or after this
     */
    public long duration(TimeStamp timeStamp) {
        return timeStamp.toTicks() - toTicks();
    }

    public long durationInSeconds(TimeStamp timeStamp) {
        return divide(duration(timeStamp), TICKS_IN_SECOND);
    }

    public long durationInMinutes(TimeStamp timeStamp) {
        return divide(duration(timeStamp), TICKS_IN_MINUTE);
    }

    public long durationInHours(TimeStamp timeStamp) {
        return divide(duration(timeStamp), TICKS_IN_HOUR);
    }

    /**
     *
     */
    public long durationInDays(TimeStamp timeStamp) {
        return divide(duration(timeStamp), TICKS_IN_DAY);
    }

    private static long divide(long dividend, long divisor) {
        boolean negative = dividend < 0;
        if (negative) dividend = -dividend;
        return ((dividend - (dividend % divisor)) / divisor) * (negative ? -1 : 1);
    }

    /**
     * Turns TimeStamp into ticks from January 1, 1970, 0:00:00 GMT.
     */
    @Override
    public long toTicks() {
        return totalTicks;
    }

    public class Formatter {

        private Formatter() {
        }

        public String getYear() {
            return String.format("%04d", TimeStamp.this.year);
        }

        public String getMonth() {
            return String.format("%02d", TimeStamp.this.month);
        }

        public String getDayOfMonth() {
            return String.format("%02d", TimeStamp.this.dayOfMonth);
        }

        public String getHours() {
            return String.format("%02d", TimeStamp.this.hours);
        }

        public String getMinutes() {
            return String.format("%02d", TimeStamp.this.minutes);
        }

        public String getSeconds() {
            return String.format("%02d", TimeStamp.this.seconds);
        }

        public String getTickSpillover() {
            return String.format("%02d", TimeStamp.this.tickSpillover);
        }

        public TimeStamp parse(String s) {
            return parse(s, SPLIT);
        }

        public TimeStamp parse(String s, String split) {
            int count = s.split(split).length - 1; // This count plus one is how many inputs there are
            return switch (count) {
                case 2 -> parseFromTimeWithoutSpillover(s, split);
                case 3 -> parseFromTimeWithSpillover(s, split);
                case 5 -> parseFromDateTimeWithoutSpillover(s, split);
                case 6 -> parseFromDateTimeWithSpillover(s, split);
                default -> empty();
            };
        }

        public TimeStamp parseFromTimeWithoutSpillover(String s) {
            return parseFromTimeWithoutSpillover(s, SPLIT);
        }

        public TimeStamp parseFromTimeWithSpillover(String s) {
            return parseFromTimeWithSpillover(s, SPLIT);
        }

        public TimeStamp parseFromDateTimeWithSpillover(String s) {
            return parseFromDateTimeWithSpillover(s, SPLIT);
        }

        public TimeStamp parseFromDateTimeWithoutSpillover(String s) {
            return parseFromDateTimeWithoutSpillover(s, SPLIT);
        }

        public TimeStamp parseFromTimeWithoutSpillover(String s, String split) {
            List<Long> values = Arrays.stream(s.split(split)).map(Long::parseLong).toList();
            return new TimeStamp(values.get(0), values.get(1), values.get(2));
        }

        public TimeStamp parseFromTimeWithSpillover(String s, String split) {
            List<Long> values = Arrays.stream(s.split(split)).map(Long::parseLong).toList();
            return new TimeStamp(values.get(0), values.get(1), values.get(2), values.get(3));
        }

        public TimeStamp parseFromDateTimeWithSpillover(String s, String split) {
            List<Long> values = Arrays.stream(s.split(split)).map(Long::parseLong).toList();
            return new TimeStamp(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4), values.get(5), values.get(6));
        }

        public TimeStamp parseFromDateTimeWithoutSpillover(String s, String split) {
            List<Long> values = Arrays.stream(s.split(split)).map(Long::parseLong).toList();
            return new TimeStamp(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4), values.get(5));
        }

        public String format() {
            return format(SPLIT);
        }

        public String formatWithSpillover() {
            return formatWithSpillover(SPLIT);
        }

        public String formatWithoutSpillover() {
            return formatWithoutSpillover(SPLIT);
        }

        public String formatTime() {
            return formatTime(SPLIT);
        }

        public String formatTimeWithSpillover() {
            return formatTimeWithSpillover(SPLIT);
        }

        public String formatTimeWithoutSpillover() {
            return formatTimeWithoutSpillover(SPLIT);
        }

        public String formatDate() {
            return formatDate(DATE_SPLIT);
        }

        public String format(String split) {
            return TimeStamp.this.year != 0 ?
                (TimeStamp.this.tickSpillover != 0 ?
                    formatWithSpillover(split) :
                    formatWithoutSpillover(split)) :
                (formatTime(split));
        }

        public String formatWithSpillover(String split) {
            return (formatWithoutSpillover(split) + split + getTickSpillover());
        }

        public String formatWithoutSpillover(String split) {
            return (formatDate(split) + split + formatTimeWithoutSpillover(split));
        }

        public String formatTime(String split) {
            return TimeStamp.this.tickSpillover != 0 ? formatTimeWithSpillover(split) :
                formatTimeWithoutSpillover(split);
        }

        public String formatTimeWithSpillover(String split) {
            return (formatTimeWithoutSpillover(split) + split + getTickSpillover());
        }

        public String formatTimeWithoutSpillover(String split) {
            return (getHours() + split + getMinutes() + split + getSeconds());
        }

        public String formatDate(String split) {
            return Locale.ROOT.equals(Locale.US) ?
                (formatDateUS(split)) :
                (getDayOfMonth() + split + getMonth() + split + getYear());
        }

        public String formatDateUS(String split) {
            return getMonth() + split + getDayOfMonth() + split + getYear();
        }

        public String formatDateUS() {
            return formatDateUS(DATE_SPLIT);
        }

    }

    public TimeStamp setYear(long year) {
        this.year = year;
        return this;
    }

    public TimeStamp setMonth(long month) {
        this.month = month;
        return this;
    }

    public TimeStamp setDayOfMonth(long dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        return this;
    }

    public TimeStamp setTotalTicks() {
        this.totalTicks =
            tickSpillover +
                secondsToTicks(seconds +
                    minutesToSeconds(minutes +
                        hoursToMinutes(hours +
                            daysToHours(dayOfMonth +
                                monthsToDays(month +
                                    yearsToMonths(year))))));
        return this;
    }

    public static long yearsToMonths(long years) {
        return years * 12;
    }

    public static long monthsToDays(long months) {
        return months * 30;
    }

    public static long daysToHours(long days) {
        return days * 24;
    }

    public static long hoursToMinutes(long hours) {
        return hours * 60;
    }

    public static long minutesToSeconds(long minutes) {
        return minutes * 60;
    }

    public static long secondsToTicks(long seconds) {
        return seconds * 20;
    }

    public static long monthsToYears(long months) {
        return months / 12;
    }

    public static long daysToMonths(long days) {
        return days / 30;
    }

    public static long hoursToDays(long hours) {
        return hours / 24;
    }

    public static long minutesToHours(long minutes) {
        return minutes / 60;
    }

    public static long secondsToMinutes(long seconds) {
        return seconds / 60;
    }

    public static long ticksToSeconds(long ticks) {
        return ticks / 20;
    }

}
