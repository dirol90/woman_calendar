package com.vio.calendar.model.date;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Helper to deal with calendar dates
 */
public class GregorianCalendarExt extends GregorianCalendar {
    /**
     *  Calculate the difference between this calendar date and a given date in days
     *
     *  @param date
     *  The date to which the difference should be calculated
     *
     *  @return
     *  The number of days between the calendar date and the given date
     */
    public int diffDayPeriods(Calendar date) {
        long endL = date.getTimeInMillis() + date.getTimeZone().getOffset( date.getTimeInMillis() );
        long startL = this.getTimeInMillis() + getTimeZone().getOffset( getTimeInMillis() );
        return (int) ((endL-startL) / (1000*60*60*24));         
    }
}
