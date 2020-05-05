package com.vio.calendar.model.date

data class Month (
     val year: Int, val month: Int, private val dayList: ArrayList<CalendarCell>
): Comparable<Month> {
    override fun compareTo(other: Month): Int {
        if (this.year > other.year) return 1
        if (this.year < other.year) return -1
        if (this.month > other.month) return 1
        if (this.month < other.month) return -1
        return 0
    }

    fun key(): Long {
        return (this.year*100 + this.month).toLong()
    }
}

