package com.vio.calendar.data.calendar.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CalendarDatabaseOpenHelper(context: Context): SQLiteOpenHelper(context, name, null, version) {

    companion object {
        const val name: String = "calendar.db"
        const val version: Int = 1
        const val createSQL: String =
            "create table data (" +
                    "type integer(3), " +
                    "date varchar(8), " +
                    "cvx integer(3), " +
                    "temp real, " +
                    "intensity integer(3)" +
                    ");"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.beginTransaction()
        db.execSQL(createSQL)
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}