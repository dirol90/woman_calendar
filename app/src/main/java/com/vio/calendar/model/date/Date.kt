package com.vio.calendar.model.date

data class Date (private val year: Int, private val month: Int, private val day: Int)
    : Comparable<Date> {

    companion object {
        const val MONTHS_IN_A_YEAR = 12
    }

    init {
        if (month > MONTHS_IN_A_YEAR || month <  0) {
            throw IllegalStateException("Month must between 1 - $MONTHS_IN_A_YEAR")
        }
        if (day > daysInMonth(month, year)) {
            throw IllegalStateException("Day $day not valid in month $month of year $year")
        }
    }

    override operator fun compareTo(other: Date): Int {
        if (this.year > other.year) return 1
        if (this.year < other.year) return -1
        if (this.month > other.month) return 1
        if (this.month < other.month) return -1
        if (this.day > other.day) return 1
        if (this.day < other.day) return -1
        return 0
    }

    fun getYear() = year

    fun getMonth() = month

    fun getDay() = day

    operator fun inc(): Date {

        return when {
            (day > 1)
            -> Date(year, month, day - 1)
            (month > 1)
            -> Date(year, month - 1, daysInMonth(month - 1, year))
            else -> Date(year - 1, MONTHS_IN_A_YEAR, daysInMonth(MONTHS_IN_A_YEAR, year - 1))
        }
    }

    operator fun dec(): Date {
        return when {
            (day > 1)
            -> Date(year, month, day - 1)
            (month > 1)
            -> Date(year, month - 1, daysInMonth(month - 1, year))
            else -> Date(year - 1, MONTHS_IN_A_YEAR, daysInMonth(MONTHS_IN_A_YEAR, year - 1))
        }

        var date = when {
            (day < daysInMonth(month, year))
            -> Date(year, month, day + 1)
            (month < MONTHS_IN_A_YEAR)
            -> Date(year, month + 1, 1)
            else -> Date(year + 1, 1, 1)
        }
    }


    //decrease date on one month and set day to one

    //TODO if we're decreasing on 13 month's for example

    fun decMonths(i: Int): Date {
        return when {
            (month > i)
            -> Date(year, month - i, 1)
            else -> Date(year - 1, MONTHS_IN_A_YEAR - (i - month), 1)
        }
    }

    //TODO if we're increasing on 13 month's for example

    fun incMonth(i: Int): Date {
        return when {
            (month + i - 1 < MONTHS_IN_A_YEAR)
            -> Date(year, month + i, 1)
            else -> Date(year + 1, i - (MONTHS_IN_A_YEAR - month), 1)
        }
    }

    operator fun rangeTo(that: Date) =
        DateRange(this, that)
}

