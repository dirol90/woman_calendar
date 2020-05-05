package com.vio.calendar.model.date

import com.vio.calendar.db.PeriodicalDatabase.DayEntry

data class CalendarCell(
    var type: Int = DayEntry.EMPTY,
    var day: Int = 1,
    var month: Int = 1,
    var year: Int = 1,
    var intensity: Int = 1,
    var dayofcycle: Int = 0,
    var notes: Boolean = false,
    var iscurrent: Boolean = false,
    var intercourse: Boolean = false)