package com.vio.calendar.model.date

import com.vio.calendar.model.prefs.UserInfoPreferences

class DateIterator(start: Date, private val endInclusive: Date) : Iterator<Date> {

    private var current = start

    private val lengthCycle = UserInfoPreferences.getUserInfo().lengthCycle
    private val lengthMenstrualCycle = UserInfoPreferences.getUserInfo().lengthMenstrual

    private var counter = 0

    override fun hasNext(): Boolean {
        return current <= endInclusive
    }

    override fun next(): Date {
        return current++
    }
}