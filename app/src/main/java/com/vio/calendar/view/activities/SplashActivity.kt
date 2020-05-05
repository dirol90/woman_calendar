package com.vio.calendar.view.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.vio.calendar.PreferenceHelper
import com.vio.calendar.PreferenceHelper.defaultPrefs
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.setTransparentStatusBar
import com.vio.calendar.ui.license.LicenseActivity
import com.vio.calendar.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class SplashActivity : LocalizationActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    private lateinit var prefs: SharedPreferences


    private fun hideSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    internal val mRunnable: Runnable = Runnable {

        if (prefs.getBoolean("license", false)) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LicenseActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = PreferenceHelper.defaultPrefs(applicationContext)
        val res = this.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(CalendarApplication.code.toLowerCase()))
        res.updateConfiguration(conf, dm)

        super.onCreate(savedInstanceState)
        this.setTransparentStatusBar()
        setContentView(R.layout.activity_splash)

        hideSystemUI()

        prefs = defaultPrefs(applicationContext)

        imageViewSplash.animate().alpha(1.0f).duration = 1500
        //Initializing the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
    }
}