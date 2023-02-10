package org.vizzoid.utils;

import java.time.LocalDate;

public class YearLength {

    /**
     * Returns the total day amount for the year as it depends on whether the year is a leap year or not
     */
    public int of(int year) {
        return year % 4 == 0 ? 366 : 365;
    }

    /**
     * Returns the total day amount from the start year for the input amount of years
     */
    public long mod(int startYear, int years) {
        int days = 0;
        for (int i = 0; i < years; i++) {
            days += of(startYear);
            startYear++;
        }
        return days;
    }

    /**
     * Returns the total day amount from the current year for the input amount of years
     */
    public long mod(int years) {
        return mod(LocalDate.now().getYear(), years);
    }

}
