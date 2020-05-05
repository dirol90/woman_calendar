package com.vio.calendar.data.calendar

import java.util.*

@Deprecated("12.09.2019 CHANGED TO FIREBASE STORAGE")
class GregorianCalendarExt: GregorianCalendar() {

    /**
     * Calculate the difference between this calendar date and a given date in days
     *
     * @param date
     * The date to which the difference should be calculated
     *
     * @return
     * The number of days between the calendar date and the given date
     */
    fun diffDayPeriods(date: Calendar): Int {
        val endL = date.timeInMillis + date.timeZone.getOffset(date.timeInMillis)
        val startL = this.timeInMillis + timeZone.getOffset(timeInMillis)
        return ((endL - startL) / (1000 * 60 * 60 * 24)).toInt()
    }
}