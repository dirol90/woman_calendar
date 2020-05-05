package com.vio.calendar.ui.main

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.google.android.gms.ads.*
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vio.calendar.PreferenceHelper.defaultPrefs
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
//import com.vio.calendar.data.user.UserRepository
//import com.vio.calendar.data.user.model.UserData
import com.vio.calendar.model.dialog.LanguageItem
import com.vio.calendar.setTransparentStatusBar
import com.vio.calendar.ui.articles.ArticlesFragment
import com.vio.calendar.ui.calendar.CalendarFragment
import com.vio.calendar.view.fragments.PrefsFragment
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardItem
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.view.get
import com.vio.calendar.interfaces.ClickInterface
import com.vio.calendar.ui.review.ReviewFragment
import com.vio.calendar.utils.LocaleUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*

class MainActivity : LocalizationActivity(), RewardedVideoAdListener, ClickInterface {

    var isReviewFragmentOpenedBuThisSession = false

    override fun menuItemPerformClick() {
        this.findViewById<BottomNavigationView>(R.id.nav_view).selectedItemId = R.id.navigation_advices
    }

    private var databaseHelper = CalendarApplication.getDatabaseHelper()

    lateinit var prefs: SharedPreferences
    private var adsCounter = 1

    companion object {
        fun reload(contex: Context) {

            (contex as Activity).recreate()
        }

        var mRewardedVideoAd: RewardedVideoAd? = null
    }

    private lateinit var preferences: SharedPreferences

    private val articlesFragment = ArticlesFragment()
    private val todayFragment = CalendarFragment()
    private val prefsFragment = PrefsFragment()


    public val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->

            hideBottomPanel()

            when (item.itemId) {

                R.id.navigation_today -> {
                    switchToFragment(todayFragment)
                    return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_advices -> {
                    switchToFragment(articlesFragment)
                    return@OnNavigationItemSelectedListener true

                }
                R.id.navigation_more -> {
                    switchToFragment(prefsFragment)
                    return@OnNavigationItemSelectedListener true

                }
            }
            false
        }

    private fun switchToFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frame, fragment).commit()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleUtils.onAttach(base, CalendarApplication.code))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setTransparentStatusBar()
        todayFragment.setInterface(this)
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        prefs = defaultPrefs(applicationContext)
        val res = this.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.setLocale(Locale(CalendarApplication.code.toLowerCase()))
        res.updateConfiguration(conf, dm)

        setContentView(R.layout.activity_main)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor =
                ContextCompat.getColor(applicationContext, R.color.transparent)
        }

        databaseHelper.setOption("launch", 1)


        hideBottomPanel()


        //LanguagePrefs
        CalendarApplication.languages = mutableListOf<LanguageItem>()
        CalendarApplication.languages.add(LanguageItem(R.drawable.ic_english, "en", "English"))
        CalendarApplication.languages.add(LanguageItem(R.drawable.ic_spain, "es", "Spain"))
        CalendarApplication.languages.add(LanguageItem(R.drawable.ic_russia, "ru", "Русский"))

        preferences = defaultPrefs(applicationContext)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        switchToFragment(todayFragment)

        mRewardedVideoAd!!.rewardedVideoAdListener = this
        loadRewardedVideoAd()

        var appOpeningCounter = 0
        appOpeningCounter = prefs.getInt("AppOpeningCounter", 0)

        prefs.edit().putInt("AppOpeningCounter", appOpeningCounter+1).apply()

        var appReviewWasClicked = false
        appReviewWasClicked = prefs.getBoolean("AppReviewPressed", false)

        if (appOpeningCounter >= 2 && !isReviewFragmentOpenedBuThisSession && !appReviewWasClicked){
            isReviewFragmentOpenedBuThisSession = true
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frame, ReviewFragment(prefs)).addToBackStack("Main").commit()
        }

        var menuFragment = intent.getStringExtra("push")

        if (menuFragment != null && menuFragment == "isOpenNewsFragment"){
            this.findViewById<BottomNavigationView>(R.id.nav_view).selectedItemId = R.id.navigation_advices
        }

    }

    override fun onPause() {
        super.onPause()
        hideBottomPanel()
    }

    override fun onResume() {
        super.onResume()
        hideBottomPanel()
    }


    private fun hideBottomPanel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }




    private fun loadRewardedVideoAd() {
        mRewardedVideoAd!!.loadAd(
//            "ca-app-pub-3940256099942544/5224354917", // test ID
            "ca-app-pub-1890073619173649/7868362523",
            AdRequest.Builder().build()
        )
    }

    override fun onRewardedVideoAdClosed() {
        loadRewardedVideoAd()
    }

    override fun onRewardedVideoAdLeftApplication() {
        loadRewardedVideoAd()
    }

    override fun onRewardedVideoAdLoaded() {

    }

    override fun onRewardedVideoAdOpened() {

    }

    override fun onRewardedVideoCompleted() {
        hideBottomPanel()
    }

    override fun onRewarded(p0: RewardItem?) {

    }

    override fun onRewardedVideoStarted() {

    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        loadRewardedVideoAd()
    }


}

