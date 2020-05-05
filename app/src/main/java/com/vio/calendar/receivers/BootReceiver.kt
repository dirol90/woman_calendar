package com.vio.calendar.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.vio.calendar.db.DatabaseHelper
import java.util.*

/**
 * Receives the BOOT_COMPLETED action and schedules the alarms after reboot.
 */
class BootReceiver : BroadcastReceiver() {

  private lateinit var databaseHelper: DatabaseHelper
  private var calToday = GregorianCalendar()

  private var monthCurrent = calToday.get(Calendar.MONTH) + 1
  private var yearCurrent = calToday.get(Calendar.YEAR)
  private var dayCurrent = calToday.get(Calendar.DATE)

  override fun onReceive(context: Context?, intent: Intent?) {
    if (context != null && intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
    //  databaseHelper = DatabaseHelper(context)
     // databaseHelper.setOption("option", 1)
     // databaseHelper.addPeriod(calToday)
      Log.d("BootReceiver", "VIO calendar fetched boot action")
    }
  }
}

