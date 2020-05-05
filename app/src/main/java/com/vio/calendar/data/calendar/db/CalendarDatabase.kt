package com.vio.calendar.data.calendar.db

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.vio.calendar.PreferenceHelper.defaultPrefs
import java.lang.String.format
import java.util.*

@Deprecated("12.09.2019 CHANGED TO FIREBASE STORAGE")
class CalendarDatabase(val context: Context) {

    private lateinit var db: SQLiteDatabase
    private val dayEntries = Vector<DayEntry>()


    companion object {
        const val DEFAULT_PERIOD_LENGTH: Int = 4
        const val DEFAULT_CYCLE_LENGTH: Int = 183
        const val DEFAULT_START_OF_WEEK: Int  = 1
        const val DEFAULT_DIRECT_DETAILS = false
        const val DEFAULT_SHOW_CYCLE = true
    }

    init {

    }

    @SuppressLint("Recycle")
    private fun open() {
        val dataOpenHelper = CalendarDatabaseOpenHelper(context)
        db = dataOpenHelper.writableDatabase
    }

    fun close() {
        db.close()
    }


    @SuppressLint("DefaultLocale")
    fun addPeriod(date: GregorianCalendar) {
        var statement: String

        val dateString = format(
            Locale.getDefault(), "%04d%02d%02d",
            date.get(GregorianCalendar.YEAR),
            date.get(GregorianCalendar.MONTH) + 1,
            date.get(GregorianCalendar.DAY_OF_MONTH)
        )

        val dateLocal = GregorianCalendar()
        dateLocal.time = date.time
        dateLocal.add(GregorianCalendar.DATE, -1)

        var type = getEntryType(dateLocal)

        if (type == DayEntry.PERIOD_START || type == DayEntry.PERIOD_CONFIRMED) {
            // Continue existing period_predicted to the future
            type = DayEntry.PERIOD_CONFIRMED
            db.beginTransaction()
            statement = format(
                "delete from data where date = '%s'",
                dateString
            )
            db.execSQL(statement)
            statement = format(
                "insert into data (date, type, intensity) values ('%s', %d, 1)",
                dateString,
                type
            )
            db.execSQL(statement)
            db.setTransactionSuccessful()
            db.endTransaction()
        } else {
            dateLocal.add(GregorianCalendar.DATE, +2)
            type = getEntryType(dateLocal)
            if (type == DayEntry.PERIOD_START) {
                // Continue existing period_predicted to the past
                db.beginTransaction()

                statement = format("delete from data where date = '%s'", dateString)
                db.execSQL(statement)

                statement = format("insert into data (date, type, intensity) values ('%s', %d, 2)", dateString, type)
                db.execSQL(statement)

                statement = format(
                    "update data set type=%d where date = '%s'",
                    DayEntry.PERIOD_CONFIRMED,
                    format(
                        Locale.getDefault(), "%04d%02d%02d",
                        dateLocal.get(GregorianCalendar.YEAR),
                        dateLocal.get(GregorianCalendar.MONTH) + 1,
                        dateLocal.get(GregorianCalendar.DAY_OF_MONTH)
                    )
                )
                db.execSQL(statement)

                db.setTransactionSuccessful()
                db.endTransaction()
            } else {
                // This day is a regular new period_predicted
                val periodLength: Int

                val preferences = defaultPrefs(context)
                periodLength = preferences.getInt("period_length", DEFAULT_PERIOD_LENGTH)

                type = DayEntry.PERIOD_START
                dateLocal.time = date.time
                var intensity = 2

                db.beginTransaction()
                for (day in 0 until periodLength) {

                    val dateStringLocal = format(
                        Locale.getDefault(), "%04d%02d%02d",
                        dateLocal.get(GregorianCalendar.YEAR),
                        dateLocal.get(GregorianCalendar.MONTH) + 1,
                        dateLocal.get(GregorianCalendar.DAY_OF_MONTH)
                    )

                    statement = format(
                        "insert into data (date, type, intensity) values ('%s', %d, %d)",
                        dateStringLocal,
                        type,
                        intensity
                    )
                    db.execSQL(statement)

                    type = DayEntry.PERIOD_CONFIRMED

                    // Second day gets a higher intensity, the following ones decrease it every day
                    if (day == 0)
                        intensity = 4
                    else {
                        if (intensity > 1) intensity--
                    }
                    dateLocal.add(GregorianCalendar.DATE, 1)
                }
                db.setTransactionSuccessful()
                db.endTransaction()
            }
        }
        date.add(GregorianCalendar.DATE, 1)
    }

    @SuppressLint("DefaultLocale")
    fun removePeriod(date: GregorianCalendar) {
        var statement: String

        val dateLocal = GregorianCalendar()
        dateLocal.time = date.time

        db.beginTransaction()

        // Remove period_predicted entry for the selected and all following days
        while (true) {
            val type = getEntryType(dateLocal)
            if (type == DayEntry.PERIOD_START || type == DayEntry.PERIOD_CONFIRMED) {
                statement = format(
                    "update data set type=%d where date='%s'",
                    DayEntry.EMPTY,
                    format(
                        Locale.getDefault(), "%04d%02d%02d",
                        dateLocal.get(GregorianCalendar.YEAR),
                        dateLocal.get(GregorianCalendar.MONTH) + 1,
                        dateLocal.get(GregorianCalendar.DAY_OF_MONTH)
                    )
                )
                db.execSQL(statement)
                dateLocal.add(GregorianCalendar.DATE, 1)
            } else {
                break
            }
        }

        db.setTransactionSuccessful()
        db.endTransaction()
    }

    private fun getEntryType(date: GregorianCalendar): Int {
        for (entry in dayEntries) {
            // If entry was found, then return type
            if (entry.date == date) {
                return entry.type
            }
        }

        // Fall back if month was not found, then return "empty" as type
        return 0
    }
}

