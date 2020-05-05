package com.vio.calendar.ui.calendar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.ViewContainer
import com.vio.calendar.R
import com.vio.calendar.ui.calendar.CalendarFragmentDetailed.Companion.endDate
import com.vio.calendar.ui.calendar.CalendarFragmentDetailed.Companion.startDate
import kotlinx.android.synthetic.main.fragment_calendar_detailed.*
import kotlinx.android.synthetic.main.fragment_calendar_detailed.view.*
import org.threeten.bp.LocalDate

class DayViewContainer(view: View, calendarView: CalendarView) : ViewContainer(view) {
    val textView2: TextView = view.findViewById(R.id.day_tv)
    val roundBgView: ImageView = view.findViewById(R.id.day_iv)

    lateinit var day: CalendarDay
    private val today = LocalDate.now()

    val textView = with(view.findViewById<TextView>(R.id.day_date_tv)) {
        textView2.makeInVisible()
        setOnClickListener {
            if (day.owner == DayOwner.THIS_MONTH && (day.date == today || day.date.isBefore(today) || day.date.isAfter(today))) {
                val date = day.date
                if (startDate != null) {
                    if (date < startDate || endDate != null) {
                        startDate = date
                        endDate = null
                    } else if (date != startDate) {
                        endDate = date
                    }
                } else {
                    startDate = date
                }
                calendarView.notifyCalendarChanged()
            }
        }
        return@with this as TextView
    }


}