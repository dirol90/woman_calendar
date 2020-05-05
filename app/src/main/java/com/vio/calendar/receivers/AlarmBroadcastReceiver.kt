package com.vio.calendar.receivers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.vio.calendar.PreferenceHelper.defaultPrefs
import com.vio.calendar.R
import com.vio.calendar.db.DatabaseHelper
import com.vio.calendar.db.PeriodicalDatabase.DayEntry.*
import com.vio.calendar.ui.main.MainActivity
import java.util.*


class AlarmBroadcastReceiver : BroadcastReceiver() {

    private var calToday = GregorianCalendar()

    private var monthCurrent = calToday.get(Calendar.MONTH) + 1
    private var yearCurrent = calToday.get(Calendar.YEAR)
    private var dayCurrent = calToday.get(Calendar.DATE)

    private val pink = Color.parseColor("#E6ABAD")


    override fun onReceive(context: Context, intent: Intent?) {
        val databaseHelper = DatabaseHelper(context.applicationContext)
        databaseHelper.setOption("option", 1)

        val prefs = defaultPrefs(context.applicationContext)

        var type = databaseHelper.getDayType(
            GregorianCalendar(
                yearCurrent, monthCurrent - 1, dayCurrent
            )
        )
        if (type == PERIOD_PREDICTED) {
//            databaseHelper.addPeriod(GregorianCalendar(yearCurrent, monthCurrent - 1, dayCurrent))
            if (prefs.getBoolean("notification_m_start", false)) {
                showNotificationMStart(context)
            }
        } else if (type == PERIOD_CONFIRMED) {
            val today = GregorianCalendar(
                yearCurrent, monthCurrent - 1, dayCurrent
            )
            today.add(Calendar.DATE, 1)
            var type = databaseHelper.getDayType(today)

            if (type != PERIOD_CONFIRMED) {

                if (prefs.getBoolean("notification_m_end", false)) {
                    showNotificationMEnd(context)
                }
            }
        } else if (type == OVULATION_PREDICTED || type == OVULATION_FUTURE) {
            if (prefs.getBoolean("notification_ovulation", false)) {
                showNotificationOvulation(context)
            }
        }

    }

    private fun showNotificationMEnd(context: Context) {
        val CHANNEL_ID = "com.vio.calendar.notificationMEnd"// The id of the channel.
        val name = context.resources.getString(R.string.app_name)// The user-visible name of the channel.
        val mBuilder: NotificationCompat.Builder
        val notificationIntent = Intent(context, MainActivity::class.java)
        val bundle = Bundle()
        notificationIntent.putExtras(bundle)
//        notificationIntent.putExtra("push", "isOpenNewsFragment")
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        val contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            val mChannel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(mChannel)
            mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_m_end)
                .setLights(Color.RED, 300, 300)
                .setChannelId(CHANNEL_ID)
                .setContentTitle("VIO")
        } else {
            mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_m_end)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentTitle("VIO")
        }

        mBuilder.setContentIntent(contentIntent)
        mBuilder.setContentText("Notification: period last day")
        mBuilder.setAutoCancel(true)
        mBuilder.color = pink
        mNotificationManager.notify(1, mBuilder.build())
    }

    private fun showNotificationMTwoDays(context: Context) {
        val CHANNEL_ID = "com.vio.calendar"// The id of the channel.
        val name = context.resources.getString(R.string.app_name)// The user-visible name of the channel.
        val mBuilder: NotificationCompat.Builder
        val notificationIntent = Intent(context, MainActivity::class.java)
        val bundle = Bundle()
        notificationIntent.putExtras(bundle)
//        notificationIntent.putExtra("push", "isOpenNewsFragment")
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        val contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            val mChannel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(mChannel)
            mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_m_two_days)
                .setLights(Color.RED, 300, 300)
                .setChannelId(CHANNEL_ID)
                .setContentTitle("VIO")
        } else {
            mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification_m_two_days)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentTitle("VIO")
        }

        mBuilder.setContentIntent(contentIntent)
        mBuilder.setContentText("Get Notifi")
        mBuilder.setAutoCancel(true)
        mBuilder.color = pink
        mNotificationManager.notify(1, mBuilder.build())
    }

    private fun showNotificationMStart(context: Context) {
        val CHANNEL_ID = "com.vio.calendar.notificationMStart"// The id of the channel.
        val name = context.resources.getString(R.string.app_name)// The user-visible name of the channel.
        val mBuilder: NotificationCompat.Builder
        val notificationIntent = Intent(context, MainActivity::class.java)
        val bundle = Bundle()
        notificationIntent.putExtras(bundle)
//        notificationIntent.putExtra("push", "isOpenNewsFragment")
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        val contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            val mChannel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(mChannel)
            mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_menstrual_start)
                .setLights(Color.RED, 300, 300)
                .setChannelId(CHANNEL_ID)
                .setContentTitle("VIO")
        } else {
            mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_menstrual_start)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentTitle("VIO")
        }

        mBuilder.setContentIntent(contentIntent)
        mBuilder.setContentText("Notification, period started today")
        mBuilder.setAutoCancel(true)
        mBuilder.color = pink
        mNotificationManager.notify(1, mBuilder.build())
    }

    private fun showNotificationOvulation(context: Context) {
        val CHANNEL_ID = "com.vio.calendar.notificationMEnd"// The id of the channel.
        val name = context.resources.getString(R.string.app_name)// The user-visible name of the channel.
        val mBuilder: NotificationCompat.Builder
        val notificationIntent = Intent(context, MainActivity::class.java)
        val bundle = Bundle()
        notificationIntent.putExtras(bundle)
//        notificationIntent.putExtra("push", "isOpenNewsFragment")
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        val contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            val mChannel = NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH)
            mNotificationManager.createNotificationChannel(mChannel)
            mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_ovulation)
                .setLights(Color.RED, 300, 300)
                .setChannelId(CHANNEL_ID)
                .setContentTitle("VIO")
        } else {
            mBuilder = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_ovulation)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentTitle("VIO")
        }

        mBuilder.setContentIntent(contentIntent)
        mBuilder.setContentText("Notification: ovulation today")
        mBuilder.setAutoCancel(true)
        mBuilder.color = pink
        mNotificationManager.notify(1, mBuilder.build())
    }

}