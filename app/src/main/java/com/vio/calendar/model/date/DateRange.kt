package com.vio.calendar.model.date

import com.vio.calendar.model.date.Date.Companion.MONTHS_IN_A_YEAR

class DateRange(override val start: Date, override val endInclusive: Date)
    : ClosedRange<Date>, Iterable<Date> {

    override fun iterator(): Iterator<Date> {
        return DateIterator(start, endInclusive)
    }

    fun numberOfMonth(): Int {

        if (endInclusive.getYear() == start.getYear()) {
            return endInclusive.getMonth() - start.getMonth() + 1
        }
        return MONTHS_IN_A_YEAR - start.getMonth() + endInclusive.getMonth() + 1
    }

}