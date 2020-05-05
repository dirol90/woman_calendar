package com.vio.calendar.db

import android.app.backup.BackupManager
import android.content.Context
import com.vio.calendar.PreferenceHelper
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.model.date.CalendarCell
import org.threeten.bp.LocalDate
import java.lang.Exception
import java.util.*

class DatabaseHelper(context: Context) {

    private var dbMain: PeriodicalDatabase = PeriodicalDatabase(context)
    private val bm = BackupManager(context)
    private var cycle = -1
    private var period = -1

    init {
        dbMain.restorePreferences()

        if (CalendarApplication.prefs == null) {
            CalendarApplication.prefs =
                PreferenceHelper.defaultPrefs(context)
        }

        try {
            period = CalendarApplication.prefs!!.getInt("period_length", 3)
            cycle = CalendarApplication.prefs!!.getInt("maximum_cycle_length", 28)
        } catch (e: Exception){e.printStackTrace()}


        setOption("launch", 1)
    }


    fun updateDataInDatabase(cylce: Int, period: Int) {
            dbMain.loadCalculatedDataCustomMethod(cylce, period)
            bm.dataChanged()
    }


//    fun addPeriod(dateStart : GregorianCalendar) {
//        dbMain.addPeriod(dateStart)
//        dbMain.loadCalculatedData()
//        bm.dataChanged()
//    }

    fun addPeriodCustom(date1: LocalDate?, date2: LocalDate?) {
        try {
            period = CalendarApplication.prefs!!.getInt("period_length", 3)
            cycle = CalendarApplication.prefs!!.getInt("maximum_cycle_length", 28)
        } catch (e: Exception){e.printStackTrace()}


            dbMain.addPeriodCustom(
                GregorianCalendar(
                    date1!!.getYear(),
                    date1!!.getMonthValue() - 1,
                    date1!!.getDayOfMonth()
                ), GregorianCalendar(
                    date2!!.getYear(),
                    date2!!.getMonthValue() - 1,
                    date2!!.getDayOfMonth()
                )
            )
            dbMain.loadCalculatedDataCustomMethod(cycle, period)
            bm.dataChanged()

        dbMain.collectEntries()

    }

    fun addPeriodCustom(date1: GregorianCalendar, date2: GregorianCalendar) {
            period = CalendarApplication.prefs!!.getInt("period_length", 3)
            cycle = CalendarApplication.prefs!!.getInt("maximum_cycle_length", 28)

            dbMain.addPeriodCustom(date1, date2)
            dbMain.loadCalculatedDataCustomMethod(cycle, period)
            bm.dataChanged()

        dbMain.collectEntries()

    }

    fun removePeriod() {
        dbMain.removePeriodFully()
        bm.dataChanged()

        dbMain.collectEntries()
    }

    fun setOption(name: String, value: Int) {
        dbMain.setOption(name, value)
        bm.dataChanged()

        dbMain.collectEntries()
    }

    fun getDayType(date: GregorianCalendar): Int {
        return dbMain.getEntryType(date)
    }

    fun getDaysList(year: Int, month: Int): ArrayList<CalendarCell> {
        val list = ArrayList<CalendarCell>()

        val calendar = GregorianCalendar(year, month - 1, 1)

        var firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        firstDayOfWeek--
        if (firstDayOfWeek == 0) firstDayOfWeek = 7

        val daysCount = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        var i = 1
        while (i < firstDayOfWeek + daysCount) {

            val calendarCell = CalendarCell()

            if (i < firstDayOfWeek) {
                //adding empty
            } else {
                calendarCell.day = i - firstDayOfWeek + 1
                calendarCell.month = month
                calendarCell.year = year

                val entry = dbMain.getEntry(calendar)

                if (entry != null) {

                    calendarCell.type = entry.type
                    calendarCell.dayofcycle = entry.dayofcycle
                    calendarCell.intensity = entry.intensity

                    for (s in entry.symptoms) {
                        if (s == 1) calendarCell.intercourse = true
                        else calendarCell.notes = true
                    }

                    if (entry.notes.isNotEmpty()) calendarCell.notes = true

                } else {
                    calendarCell.type = PeriodicalDatabase.DayEntry.EMPTY
                    calendarCell.dayofcycle = 0
                }
                calendar.add(GregorianCalendar.DATE, 1)
            }

            list.add(calendarCell)
            i++
        }
        return list
    }

    fun collectDataSet(){
        dbMain.collectEntries()
    }

    fun getDaysEntry(year: Int, month: Int, day: Int): PeriodicalDatabase.DayEntry {

        val calendar = GregorianCalendar(year, month, day)
        val entry = dbMain.getEntry(calendar)
        return if (entry != null) {
            entry
        } else {
            PeriodicalDatabase.DayEntry()
        }
    }

    fun getOption(name: String, defaultvalue: Int): Int {
        dbMain.collectEntries()
        return dbMain.getOption(name, defaultvalue)
    }

//    fun removeAll(){
//        return dbMain.dayEntries.removeAllElements()
//    }

}