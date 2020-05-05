package com.vio.calendar.data.calendar.db

import com.vio.calendar.model.date.GregorianCalendarExt

data class DayEntry (
    var type: Int = 0,
    val date: GregorianCalendarExt,
    var dayofcycle: Int = 0,
    var intensity: Int = 0
) {
    companion object {
        const val EMPTY = 0
        const val PERIOD_START = 1
        const val PERIOD_CONFIRMED = 2
        const val PERIOD_PREDICTED = 3
        const val FERTILITY_PREDICTED = 4
        const val OVULATION_PREDICTED = 5
        const val FERTILITY_FUTURE = 6
        const val OVULATION_FUTURE = 7
        const val INFERTILE_PREDICTED = 8
        const val INFERTILE_FUTURE = 9
    }
}