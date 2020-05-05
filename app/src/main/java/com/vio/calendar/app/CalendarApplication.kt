package com.vio.calendar.app

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import com.akexorcist.localizationactivity.core.LocalizationApplicationDelegate
import com.flurry.android.FlurryAgent
import com.google.android.gms.ads.MobileAds
import com.jakewharton.threetenabp.AndroidThreeTen
import com.onesignal.OneSignal
import com.vio.calendar.Constants.API_KEY
import com.vio.calendar.PreferenceHelper.defaultPrefs
//import com.vio.calendar.data.user.UserRepository
import com.vio.calendar.db.DatabaseHelper
import com.vio.calendar.model.dialog.LanguageItem
import com.vio.calendar.receivers.AlarmBroadcastReceiver
import com.vio.calendar.utils.LocaleUtils
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import java.util.*
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import androidx.core.os.ConfigurationCompat
import com.flurry.android.FlurryConfig
import com.flurry.android.marketing.FlurryMarketingModule
import com.flurry.android.marketing.FlurryMarketingOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.vio.calendar.R
import com.vio.calendar.receivers.MyFlurryMessagingListener
import java.lang.reflect.InvocationTargetException


class CalendarApplication : Application() {


    private lateinit var databaseHelper: DatabaseHelper

    companion object {

        private lateinit var instance: CalendarApplication
        fun getAppContext(): Context = instance.applicationContext

        fun getDatabaseHelper(): DatabaseHelper = instance.databaseHelper

        var code: String = "es"
        var languages = mutableListOf<LanguageItem>()
        var prefs: SharedPreferences? = null
        var NOTIFICATION_CHANNEL_ID = "my_app_flurry_channel"
    }


    override fun onCreate() {
        instance = this
        AndroidThreeTen.init(this)
        super.onCreate()
        databaseHelper = DatabaseHelper(this)

        FirebaseApp.initializeApp(applicationContext)
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TOKEN", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                val token = task.result?.token

                Log.d("TOKEN", token)
            })

        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()



        var myFlurryMessagingListener = MyFlurryMessagingListener(applicationContext);

        var flurryMessagingOptions = FlurryMarketingOptions.Builder()
            .setupMessagingWithAutoIntegration()
            .withDefaultNotificationChannelId(NOTIFICATION_CHANNEL_ID)
            .withFlurryMessagingListener(myFlurryMessagingListener)
            .build()

        var marketingModule = FlurryMarketingModule(flurryMessagingOptions)

        FlurryAgent.Builder()
            .withLogEnabled(true)
            .withCaptureUncaughtExceptions(true)
            .withContinueSessionMillis(20000)
            .withLogLevel(Log.VERBOSE)
            .withModule(marketingModule)
            .build(this, "KRPFKBDBFT7GGVVS8PZ9")

        val config = YandexMetricaConfig.newConfigBuilder(API_KEY).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)

        prefs = defaultPrefs(this)

        var a = ConfigurationCompat.getLocales(Resources.getSystem().getConfiguration())
        var sysLeng = if (!a.isEmpty) {
            if (a.get(0).language.contains(
                    "es",
                    true
                )
            ) "es" else if (a.get(0).language.contains(
                    "en",
                    true
                )
            ) "en" else if (a.get(0).language.contains("ru", true)) "ru" else "en"
        } else "en"

        code = prefs!!.getString("lang", sysLeng)!!
        prefs!!.edit().putString("lang", code).apply()


        val res = this.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(code.toLowerCase()))
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            applicationContext.createConfigurationContext(conf)
//        } else {
            res
                .updateConfiguration(conf, dm)
//        }

        initAdMobSettings(this)
        startAlarmBroadcastReceiver(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleUtils.onAttach(base, code))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleUtils.onAttach(this, code)
    }


    private fun initAdMobSettings(context: Context) {
//        UserRepository(context).getSettings()
        MobileAds.initialize(this, "ca-app-pub-1890073619173649~8908748583")
    }

    private fun startAlarmBroadcastReceiver(context: Context) {
        val intent = Intent(context, AlarmBroadcastReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 30)
        calendar.set(Calendar.SECOND, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }


}