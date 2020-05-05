package com.vio.calendar.model.date

import com.vio.calendar.R

fun daysInMonth(month: Int, year: Int): Int {
    return when (month) {
        4, 6, 9, 11 -> 30
        2 -> if (leapYear(year)) 29 else 28
        else -> 31
    }
}

fun leapYear(year: Int): Boolean {
    return when {
        year % 400 == 0 -> true
        year % 100 == 0 -> false
        else -> year % 4 == 0
    }
}
//TODO understand with algorithm

///Monday - 0, Sunday - 6
fun dayOfWeek(y: Int, month: Int, day: Int): Int {
    val temp = intArrayOf(0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4)
    var year = y
    if (month < 3) year -= 1
    return ((year + (year/4) - (year/100) + (year/400) + temp[month-1] + day) % 7) - 1
}


fun monthStringId(month: Int): Int {

    return when (month) {
        1 -> R.string.jan
        2 -> R.string.feb
        3 -> R.string.mar
        4 -> R.string.apr
        5 -> R.string.may
        6 -> R.string.jun
        7 -> R.string.jul
        8 -> R.string.aug
        9 -> R.string.sep
        10 -> R.string.oct
        11 -> R.string.nov
        12 -> R.string.dec
        else -> R.string.jan
    }
}

fun Date.setMaximumDayInMonth(): Date {
    return Date(this.getYear(), this.getMonth(), daysInMonth(this.getMonth(), this.getYear()))
}
