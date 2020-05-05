package com.vio.calendar.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.vio.calendar.R
import com.kizitonwose.calendarview.model.*
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.db.DatabaseHelper
import com.vio.calendar.db.PeriodicalDatabase.DayEntry.*
import com.vio.calendar.model.date.CalendarCell
import com.vio.calendar.ui.main.MainActivity
import com.vio.calendar.view.activities.StepsActivity
import kotlinx.android.synthetic.main.calendar_header.view.*
import kotlinx.android.synthetic.main.fragment_calendar_detailed.*
import org.threeten.bp.LocalDate
import org.threeten.bp.YearMonth
import org.threeten.bp.format.TextStyle
import org.threeten.bp.temporal.ChronoUnit
import org.threeten.bp.temporal.WeekFields
import java.util.*


class CalendarFragmentDetailed(var accessForEditing: Boolean) : Fragment() {

    private val today = LocalDate.now()
    private var daysList = mutableListOf<CalendarCell>()

    companion object {
        var startDate: LocalDate? = null
        var endDate: LocalDate? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar_detailed, container, false)
    }


    var dateStartForEditing: CalendarDay? = null
    var dateEndForEditing: CalendarDay? = null
    var futureEndDate: LocalDate? = null

    var adView : AdView? = null
    private fun initAds(): AdView {

        adView = AdView(context)
//            adView!!.adUnitId = "ca-app-pub-3940256099942544/6300978111" //-- test id
        adView!!.adUnitId = "ca-app-pub-1890073619173649/4402228951"
        adView!!.adListener = object : AdListener() {

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

        adView!!.adSize = AdSize.SMART_BANNER


        adView!!.loadAd(AdRequest.Builder().addTestDevice("22A3ECE5BE9E7A2422F93F23C9F905C8").build())
        return adView!!
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        CalendarApplication.getDatabaseHelper().collectDataSet()

        if (!accessForEditing) {
            save_iv.visibility = View.INVISIBLE
            exit_iv.setOnClickListener {
                startDate = null
                endDate = null

                val userCalendarDetailedFragment = CalendarFragment()
                val transaction =
                    (context as FragmentActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_frame, userCalendarDetailedFragment).commit()

            }
        } else {
            exit_iv.setOnClickListener {
                startDate = null
                endDate = null

                val userCalendarDetailedFragment = CalendarFragment()
                val transaction =
                    (context as FragmentActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_frame, userCalendarDetailedFragment).commit()

            }

            save_iv.setOnClickListener {
                if (startDate != null && endDate != null) {

                    CalendarApplication.getDatabaseHelper().addPeriodCustom(
                        startDate!!,
                        endDate!!
                    )

                }
                startDate = null
                endDate = null

                val userCalendarDetailedFragment = CalendarFragment()
                val transaction =
                    (context as FragmentActivity).supportFragmentManager.beginTransaction()
                transaction.replace(R.id.main_frame, userCalendarDetailedFragment).commit()

            }
        }

        val daysOfWeek = daysOfWeekFromLocale()
        view.findViewById<LinearLayout>(R.id.legendLayout).children.forEachIndexed { index, view ->
            (view as TextView).apply {
                text = daysOfWeek[index].getDisplayName(
                    TextStyle.SHORT,
                    Locale(CalendarApplication.code)
                ).toString()
            }
        }

        calendarView.setup(YearMonth.now(), YearMonth.now().plusMonths(10), daysOfWeek.first())

        calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view, calendarView)
            override fun bind(container: DayViewContainer, day: CalendarDay) {

                var dayEntry = CalendarApplication.getDatabaseHelper().getDaysEntry(
                    day.date.year,
                    day.date.month.value - 1,
                    day.date.dayOfMonth
                )

                container.day = day
                val textView = container.textView
                val roundBgView = container.roundBgView

                textView.text = null
                textView.background = null
                roundBgView.makeInVisible()

                var isOpenedPeriod = dateStartForEditing != null && dateEndForEditing != null && futureEndDate != null && day.date > dateStartForEditing!!.date && day.date <= futureEndDate
                if (!(dateStartForEditing != null && dateEndForEditing != null && futureEndDate != null)) {isOpenedPeriod = true}

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.text = day.day.toString()

                    if (day.date == today) {
                        if (dateEndForEditing == null) {
                            dateEndForEditing = day
                            futureEndDate = dateEndForEditing?.date?.plusDays(14)
                        }
                    }

                    when (dayEntry.type) {
                        FERTILITY_PREDICTED, FERTILITY_FUTURE -> {
                            roundBgView.makeVisible()
                            roundBgView.setBackgroundResource(R.drawable.circle_3)
                            if (day.date == today) {
                                textView.setTextColorRes(R.color.colorAccent)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.circle_3_current_day)
                            }
                        }
                        OVULATION_PREDICTED, OVULATION_FUTURE -> {
                            roundBgView.makeVisible()
                            roundBgView.setBackgroundResource(R.drawable.circle_3)
                            if (day.date == today) {
                                textView.setTextColorRes(R.color.colorAccent)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.circle_3_current_day)
                            }
                        }
                        PERIOD_PREDICTED -> {
                            roundBgView.makeVisible()
                            roundBgView.setBackgroundResource(R.drawable.circle_4)
                            if (day.date == today) {
                                textView.setTextColorRes(R.color.colorAccent)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.circle_4_current_day)
                            }
                        }

                        PERIOD_START, PERIOD_CONFIRMED -> {
                            roundBgView.makeVisible()
                            roundBgView.setBackgroundResource(R.drawable.circle_2)
                            if (dateStartForEditing == null) {
                                dateStartForEditing = day
                            } else if (day.date > dateStartForEditing!!.date) {
                                dateStartForEditing = day
                            }

                            if (day.date == today) {
                                textView.setTextColorRes(R.color.colorAccent)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.circle_1)
                            }
                        }
                        INFERTILE_FUTURE, INFERTILE_PREDICTED -> {
                            if (day.date == today) {
                                textView.setTextColorRes(R.color.colorAccent)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.circle_1)
                            }
                        }
                        EMPTY -> {
                            if (day.date == today) {
                                textView.setTextColorRes(R.color.colorAccent)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.circle_1)
                            }
                        }
                    }



                    if (accessForEditing && isOpenedPeriod) {
                        when {
                            startDate == day.date && endDate == null -> {
                                textView.setTextColorRes(R.color.color_1_white)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.circle_2)
                            }
                            startDate != null && endDate != null && (day.date >= startDate && day.date <= endDate) -> {
                                textView.setTextColorRes(R.color.color_1_white)
                                roundBgView.makeVisible()
                                roundBgView.setBackgroundResource(R.drawable.circle_2)
                            }
                            day.date == today -> {
                                textView.setTextColorRes(R.color.colorAccent)
                                roundBgView.makeVisible()
                                if (dayEntry.type == PERIOD_PREDICTED) {
                                    roundBgView.setBackgroundResource(R.drawable.circle_4_current_day)
                                } else if (dayEntry.type == FERTILITY_PREDICTED || dayEntry.type == FERTILITY_FUTURE ||
                                    dayEntry.type == OVULATION_PREDICTED || dayEntry.type == OVULATION_FUTURE
                                ) {
                                    roundBgView.setBackgroundResource(R.drawable.circle_3_current_day)
                                } else {
                                    roundBgView.setBackgroundResource(R.drawable.circle_1)
                                }
                            }
                            else -> textView.setTextColorRes(R.color.colorAccent)
                        }
                    }
                } else {
                    if (accessForEditing && isOpenedPeriod) {
                        val startDate = startDate
                        val endDate = endDate
                        if (startDate != null && endDate != null) {
                            if ((day.owner == DayOwner.PREVIOUS_MONTH
                                        && startDate.monthValue == day.date.monthValue
                                        && endDate.monthValue != day.date.monthValue) ||
                                (day.owner == DayOwner.NEXT_MONTH
                                        && startDate.monthValue != day.date.monthValue
                                        && endDate.monthValue == day.date.monthValue) ||
                                (startDate < day.date && endDate > day.date
                                        && startDate.monthValue != day.date.monthValue
                                        && endDate.monthValue != day.date.monthValue)
                            ) {
                                roundBgView.setBackgroundResource(R.drawable.circle_2)
                            }
                        }
                    }
                }
            }
        }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = view.headerText
            val llForMonthAds: LinearLayout = view.findViewById(R.id.adView)
        }

        calendarView.monthHeaderBinder = object : MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                container.textView.text = "${month.yearMonth.month.getDisplayName(
                    TextStyle.FULL,
                    Locale(CalendarApplication.code)
                ).toLowerCase().capitalize()} ${month.year}"
                container.llForMonthAds.removeAllViews()
                container.llForMonthAds.addView(initAds())


            }
        }

        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = WeekFields.of(Locale.getDefault()).firstDayOfWeek
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        calendarView.scrollToMonth(currentMonth)
        calendarView.inDateStyle = InDateStyle.ALL_MONTHS

    }

//    private fun getCurrentDaysList(){
//        daysList = DatabaseHelper(context).getDaysList()
//    }


//        initDayNames()
//        calculateFirstDayOfWeek()
//        headerButton.setOnClickListener {
//            if (calendarCellCurrent.type != PERIOD_CONFIRMED && calendarCellCurrent.type != PERIOD_START) {
//                databaseHelper.addPeriod(
//                    GregorianCalendar(yearCurrent, monthCurrent - 1, calendarCellCurrent.day)
//                )
//            } else {
//                databaseHelper.removePeriod(
//                    GregorianCalendar(
//                        yearCurrent,
//                        monthCurrent - 1,
//                        calendarCellCurrent.day
//                    )
//                )
//            }
//            headerSet()
//            if (firstIsShowing) updateFirstCalendar() else updateSecondCalendar()
//        }
//
//        calendarRecyclerFirst.layoutManager = CalendarGridLayoutManager(context, 7)
//        calendarRecyclerSecond.layoutManager = CalendarGridLayoutManager(context, 7)
//
//        calendarRecyclerSecond.isNestedScrollingEnabled = false
//        calendarRecyclerFirst.isNestedScrollingEnabled = false
//
//
//        firstConstraint.setGestureListener(
//            { goNext() },
//            { goPrev() }
//        )
//
//        secondConstraint.setGestureListener(
//            { goNext() },
//            { goPrev() }
//        )
//
//        initCalendar()
//
//        var type = databaseHelper.getDayType(
//            GregorianCalendar(
//                yearCurrent, monthCurrent - 1, dayCurrent
//            )
//        )
//        calendarCellCurrent =
//            CalendarCell(type = type, year = yearCurrent, month = monthCurrent, day = dayCurrent)
//
//        headerSet()


//    private fun headerSet() {
//
//        val days = (24 * 60 * 60 * 1000)
//        var currentMoment = System.currentTimeMillis()
//        dates_ll.removeAllViews()
//
//        var formatter = SimpleDateFormat("LLLL", Locale(MainActivity().code)).format(currentMoment).capitalize()
//        var monthName = formatter.format(Date(currentMoment))
//        current_month.text = monthName
//
//        for (i in 0 until 7){
//            currentMoment = System.currentTimeMillis() + days * i
//            formatter = SimpleDateFormat("d", Locale.getDefault()).format(currentMoment)
//
//            val dateValue = formatter.format(Date(currentMoment))
//
//            formatter = SimpleDateFormat("E", Locale(MainActivity().code)).format(currentMoment)
//            val dateName = formatter.format(Date(currentMoment))
//
//            val view = LayoutInflater.from(context).inflate(R.layout.item_day, null)
//
//            if (i == 0) {
//                view.findViewById<ImageView>(R.id.day_iv).setBackgroundResource(R.drawable.circle_1)
//            } else {
//                view.findViewById<ImageView>(R.id.day_iv).setBackgroundResource(android.R.color.transparent)
//            }
//
//            view.findViewById<TextView>(R.id.day_date_tv).text = dateValue
//            view.findViewById<TextView>(R.id.day_tv).text = dateName
//
//            val param = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT,
//                1.0f
//            )
//
//            view.layoutParams = param
//
//            dates_ll.addView(view)
//        }
//
//
//        titleHeader.text =
//            calendarCellCurrent.day.toString()// + " " + getText(getMonthNameForTitle(monthCurrent))
//
//        Log.d("CalendarFragment", "clicked calendar cell with: ${calendarCellCurrent.type}")
//

//        }
//    }
//
//    private fun updateFirstCalendar() {
//        val list = databaseHelper.getDaysList(yearCurrent, monthCurrent)
//        monthNameFirst.text = getString(getMonthNameForTitle(monthCurrent))
//
//        calendarRecyclerFirst.adapter = CalendarRecyclerAdapter(list, firstDayOfWeek) {
//            calendarCellCurrent = it
//            dayCurrent = it.day
//            headerSet()
//        }
//    }
//
//    private fun updateSecondCalendar() {
//        val list = databaseHelper.getDaysList(yearCurrent, monthCurrent)
//        monthNameSecond.text = getString(getMonthNameForTitle(monthCurrent))
//
//        calendarRecyclerSecond.adapter = CalendarRecyclerAdapter(list, firstDayOfWeek) {
//            calendarCellCurrent = it
//            dayCurrent = it.day
//            headerSet()
//        }
//    }
//
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putBoolean("firstIsShowing", firstIsShowing)
//    }
//
//    private fun initCalendar() {
//        Log.d("CalendarFragment", "init working: $firstIsShowing")
//        firstIsShowing = if (firstIsShowing) {
//            updateSecondCalendar()
//            viewFlipper.showNext()
//            false
//        } else {
//            updateFirstCalendar()
//            true
//        }
//    }
//
//    private fun goNext() {
//
//        monthCurrent++
//        if (monthCurrent > 12) {
//            monthCurrent = 1
//            yearCurrent++
//        }
//
//        calculateFirstDayOfWeek()
//
//        firstIsShowing = if (firstIsShowing) {
//            updateSecondCalendar()
//            false
//        } else {
//            updateFirstCalendar()
//            true
//        }
//
//        viewFlipper.inAnimation = AnimationHelper.inFromRightAnimation()
//        viewFlipper.outAnimation = AnimationHelper.outToLeftAnimation()
//        viewFlipper.showPrevious()
//    }
//
//    private fun goPrev() {
//        // Update calendar
//        // Update calendar
//        monthCurrent--
//        if (monthCurrent < 1) {
//            monthCurrent = 12
//            yearCurrent--
//        }
//
//        calculateFirstDayOfWeek()
//
//        firstIsShowing = if (firstIsShowing) {
//            updateSecondCalendar()
//            false
//        } else {
//            updateFirstCalendar()
//            true
//        }
//
//        viewFlipper.inAnimation = AnimationHelper.inFromLeftAnimation()
//        viewFlipper.outAnimation = AnimationHelper.outToRightAnimation()
//        viewFlipper.showNext()
//    }
//
//    private fun initDayNames() {
//        var weekDayNames = ArrayList<String>()
//
//        weekDayNames.add(getString(R.string.mon).substring(0, 1))
//        weekDayNames.add(getString(R.string.tue).substring(0, 1))
//        weekDayNames.add(getString(R.string.wed).substring(0, 1))
//        weekDayNames.add(getString(R.string.thu).substring(0, 1))
//        weekDayNames.add(getString(R.string.fri).substring(0, 1))
//        weekDayNames.add(getString(R.string.sat).substring(0, 1))
//        weekDayNames.add(getString(R.string.sun).substring(0, 1))
//
//        val dayWeekAdapter = DayWeekAdapter(context!!, weekDayNames)
//
//        weekDayNamesFirst.adapter = dayWeekAdapter
//        weekDayNamesSecond.adapter = dayWeekAdapter
//    }
//
//    private fun calculateFirstDayOfWeek() {
//        firstDayOfWeek =
//            GregorianCalendar(yearCurrent, monthCurrent - 1, 1).get(Calendar.DAY_OF_WEEK)
//        firstDayOfWeek--
//        if (firstDayOfWeek == 0) firstDayOfWeek = 7
//    }


}
