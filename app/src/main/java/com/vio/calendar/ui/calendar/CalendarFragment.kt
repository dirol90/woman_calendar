package com.vio.calendar.ui.calendar

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.model.date.CalendarCell
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*
import java.text.SimpleDateFormat
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.vio.calendar.db.PeriodicalDatabase
import com.vio.calendar.interfaces.ClickInterface
import java.lang.Exception


class CalendarFragment : Fragment() {

    private var databaseHelper = CalendarApplication.getDatabaseHelper()
    private var calToday = GregorianCalendar()

    private var monthCurrent = calToday.get(Calendar.MONTH) + 1
    private var yearCurrent = calToday.get(Calendar.YEAR)
    private var dayCurrent = calToday.get(Calendar.DATE)

    private lateinit var calendarCellCurrent: CalendarCell
    private lateinit var inter: ClickInterface

    fun setInterface(inter: ClickInterface) {
        this.inter = inter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        headerButton.setOnClickListener {
            val userCalendarDetailedFragment = CalendarFragmentDetailed(true)
            val transaction =
                (context as FragmentActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frame, userCalendarDetailedFragment).commit()
        }

        current_month.setOnClickListener {
            val userCalendarDetailedFragment = CalendarFragmentDetailed(false)
            val transaction =
                (context as FragmentActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frame, userCalendarDetailedFragment).commit()
        }

        calendar_img.setOnClickListener {
            val userCalendarDetailedFragment = CalendarFragmentDetailed(false)
            val transaction =
                (context as FragmentActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frame, userCalendarDetailedFragment).commit()
        }

        dates_ll.setOnClickListener {
            val userCalendarDetailedFragment = CalendarFragmentDetailed(false)
            val transaction =
                (context as FragmentActivity).supportFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frame, userCalendarDetailedFragment).commit()
        }


        var type = databaseHelper.getDayType(
            GregorianCalendar(
                yearCurrent, monthCurrent - 1, dayCurrent
            )
        )
        calendarCellCurrent =
            CalendarCell(type = type, year = yearCurrent, month = monthCurrent, day = dayCurrent)

        headerSet()


        try {
            setInterface(inter)
        } catch (e: Exception) {}


        view.findViewById<ConstraintLayout>(R.id.alert_layout).setOnClickListener {
            inter.menuItemPerformClick()
        }

        Handler().postDelayed(Runnable {
            view.findViewById<ConstraintLayout>(R.id.alert_layout).visibility = View.VISIBLE
        }, 5000)

        Handler().postDelayed(Runnable {
            view.findViewById<ConstraintLayout>(R.id.alert_layout).visibility = View.GONE
        }, 15000)

        initAds(view.findViewById(R.id.adView))

    }


    private fun initAds(view: LinearLayout) {

        val adView = AdView(context)
//            adView.adUnitId = "ca-app-pub-3940256099942544/6300978111" //-- test id
        adView.adUnitId = "ca-app-pub-1890073619173649/4909793826"
        adView.adListener = object : AdListener() {

            override fun onAdLoaded() {
                Log.d("MainActivity", "ad bottom: loaded")
            }

            override fun onAdFailedToLoad(errorCode: Int) {
                Log.d("MainActivity", "ad bottom: failed to load with error $errorCode")
            }

            override fun onAdOpened() {
                Log.d("MainActivity", "ad bottom: opened")
            }
        }

        adView.adSize = AdSize.SMART_BANNER

        view.removeAllViews()
        adView.loadAd(AdRequest.Builder().addTestDevice("22A3ECE5BE9E7A2422F93F23C9F905C8").build())
        view.addView(adView)
    }


    private fun headerSet() {

        val days = (24 * 60 * 60 * 1000)
        var currentMoment = System.currentTimeMillis()
        dates_ll.removeAllViews()

        var formatter =
            SimpleDateFormat("LLLL", Locale(CalendarApplication.code)).format(currentMoment)
                .capitalize()
        var monthName = formatter.format(Date(currentMoment))
        current_month.text = monthName

//        CalendarApplication.getDatabaseHelper().collectDataSet()

        for (i in 0 until 7) {
            currentMoment = System.currentTimeMillis() + days * i
            formatter =
                SimpleDateFormat("d", Locale(CalendarApplication.code)).format(currentMoment)

            val dateValue = formatter.format(Date(currentMoment))

            formatter =
                SimpleDateFormat("E", Locale(CalendarApplication.code)).format(currentMoment)
            val dateName = formatter.format(Date(currentMoment))

            val view = LayoutInflater.from(context).inflate(R.layout.item_day, null)

            val cal = Calendar.getInstance()
            cal.time = Date(currentMoment)

            var dayEntry = CalendarApplication.getDatabaseHelper().getDaysEntry(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            when (dayEntry.type) {
                PeriodicalDatabase.DayEntry.FERTILITY_PREDICTED, PeriodicalDatabase.DayEntry.FERTILITY_FUTURE -> {
                    view.findViewById<ImageView>(R.id.day_iv).makeVisible()
                    view.findViewById<ImageView>(R.id.day_iv)
                        .setBackgroundResource(R.drawable.circle_3)
                }
                PeriodicalDatabase.DayEntry.OVULATION_PREDICTED, PeriodicalDatabase.DayEntry.OVULATION_FUTURE -> {
                    view.findViewById<ImageView>(R.id.day_iv).makeVisible()
                    view.findViewById<ImageView>(R.id.day_iv)
                        .setBackgroundResource(R.drawable.circle_3)
                }
                PeriodicalDatabase.DayEntry.PERIOD_PREDICTED -> {
                    view.findViewById<ImageView>(R.id.day_iv).makeVisible()
                    view.findViewById<ImageView>(R.id.day_iv)
                        .setBackgroundResource(R.drawable.circle_4)
                }

                PeriodicalDatabase.DayEntry.PERIOD_START, PeriodicalDatabase.DayEntry.PERIOD_CONFIRMED -> {
                    view.findViewById<ImageView>(R.id.day_iv).makeVisible()
                    view.findViewById<ImageView>(R.id.day_iv)
                        .setBackgroundResource(R.drawable.circle_2)
                }
                PeriodicalDatabase.DayEntry.INFERTILE_FUTURE, PeriodicalDatabase.DayEntry.INFERTILE_PREDICTED -> {
                    view.findViewById<ImageView>(R.id.day_iv).makeInVisible()
                }
                PeriodicalDatabase.DayEntry.EMPTY -> {
                    view.findViewById<ImageView>(R.id.day_iv).makeInVisible()
                }
            }


            if (i == 0) {
                view.findViewById<TextView>(R.id.day_tv)
                    .setTextColorRes(R.color.colorAccent)
                view.findViewById<ImageView>(R.id.day_iv).makeVisible()
                if (dayEntry.type == PeriodicalDatabase.DayEntry.PERIOD_PREDICTED) {
                    view.findViewById<ImageView>(R.id.day_iv)
                        .setBackgroundResource(R.drawable.circle_4_current_day)
                } else if (dayEntry.type == PeriodicalDatabase.DayEntry.FERTILITY_PREDICTED || dayEntry.type == PeriodicalDatabase.DayEntry.FERTILITY_FUTURE ||
                    dayEntry.type == PeriodicalDatabase.DayEntry.OVULATION_PREDICTED || dayEntry.type == PeriodicalDatabase.DayEntry.OVULATION_FUTURE
                ) {
                    view.findViewById<ImageView>(R.id.day_iv)
                        .setBackgroundResource(R.drawable.circle_3_current_day)
                } else {
                    view.findViewById<ImageView>(R.id.day_iv)
                        .setBackgroundResource(R.drawable.circle_1)
                }
            }


            view.findViewById<TextView>(R.id.day_date_tv).text = dateValue
            view.findViewById<TextView>(R.id.day_tv).text = dateName

            val param = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )

            view.layoutParams = param

            dates_ll.addView(view)

//
        }


        var dayMoment: Long = System.currentTimeMillis()
        var startMoment: Long = System.currentTimeMillis()
        CalendarApplication.getDatabaseHelper().collectDataSet()

        for ((_, _) in (0 until 90).withIndex()) {
            dayMoment += 86400000
            val cal = Calendar.getInstance()
            cal.timeInMillis = dayMoment


            val dayEntry = CalendarApplication.getDatabaseHelper().getDaysEntry(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DATE)
            )

            println("${cal.get(Calendar.YEAR)} ${cal.get(Calendar.MONTH)}  ${cal.get(Calendar.DATE)} ${dayEntry.type} ${dayMoment}")

            if (dayEntry.type == PeriodicalDatabase.DayEntry.PERIOD_PREDICTED) {

                cicle_start_tv.text = SimpleDateFormat(
                    "E, d LLLL",
                    Locale(CalendarApplication.code)
                ).format(dayMoment).capitalize()
                println("DAYS MOMENT $dayMoment")
                println("START MOMENT $startMoment")
                println("VALUE MOMENT ${((dayMoment - startMoment) / days).toInt()}")
                titleHeader.text = ((dayMoment - startMoment) / days).toInt().toString()
                break
            }
        }


        Log.d("CalendarFragment", "clicked calendar cell with: ${calendarCellCurrent.type}")
    }

}
