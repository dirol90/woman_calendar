package com.vio.calendar.view.adapters

import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.db.DatabaseHelper
import com.vio.calendar.db.PeriodicalDatabase.DayEntry.*
import com.vio.calendar.inflate
import com.vio.calendar.model.date.CalendarCell
import com.vio.calendar.ui.calendar.CalendarFragmentDetailed
import com.vio.calendar.ui.calendar.makeVisible
import com.vio.calendar.ui.calendar.setTextColorRes
import com.vio.calendar.underline
import kotlinx.android.synthetic.main.day_item_row.view.*
import kotlinx.android.synthetic.main.item_day.view.*
import java.util.*


class CalendarRecyclerAdapter(
    private val days: ArrayList<CalendarCell>,
    private val firstDay: Int,
    private val listener: (CalendarCell) -> Unit
) :
    RecyclerView.Adapter<CalendarRecyclerAdapter.DayViewHolder>() {

    private val calToday = GregorianCalendar()
    private val dayToday = calToday.get(GregorianCalendar.DATE)
    private val monthToday = calToday.get(GregorianCalendar.MONTH) + 1
    private val yearToday = calToday.get(GregorianCalendar.YEAR)
    private var databaseHelper = CalendarApplication.getDatabaseHelper()
    private var date: GregorianCalendar? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        var inflatedView = parent.inflate(R.layout.item_day)
        return DayViewHolder(inflatedView)
    }


    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        holder.bindItem(day, listener, position)
    }


    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(calendarCell: CalendarCell, listener: (CalendarCell) -> Unit, position: Int) {

            itemView.day_tv.visibility = View.GONE
            itemView.day_iv.visibility = View.INVISIBLE

            if (position + 1 >= firstDay) {


                itemView.day_date_tv.text = calendarCell.day.toString()

                if (calendarCell.day == dayToday &&
                    calendarCell.month == monthToday &&
                    calendarCell.year == yearToday
                ) calendarCell.iscurrent = true

                when (calendarCell.type) {
                    FERTILITY_PREDICTED, FERTILITY_FUTURE -> {
                        itemView.day_iv.makeVisible()
                        itemView.day_iv.setBackgroundResource(R.drawable.circle_3)
                    }
                    OVULATION_PREDICTED, OVULATION_FUTURE -> {
                        itemView.day_iv.makeVisible()
                        itemView.day_iv.setBackgroundResource(R.drawable.circle_3)
                    }
                    PERIOD_PREDICTED -> {
                        itemView.day_iv.makeVisible()
                        itemView.day_iv.setBackgroundResource(R.drawable.circle_4)
                    }

                    PERIOD_START, PERIOD_CONFIRMED -> {
                        itemView.day_iv.makeVisible()
                        itemView.day_iv.setBackgroundResource(R.drawable.circle_2)
                    }
                    INFERTILE_FUTURE, INFERTILE_PREDICTED -> {

                    }
                    EMPTY -> {

                    }
                }

                    if (calendarCell.iscurrent) {
                        itemView.day_date_tv.setTextColorRes(R.color.colorAccent)
                        itemView.day_iv.makeVisible()
                        if (calendarCell.type == PERIOD_PREDICTED) {
                            itemView.day_iv.setBackgroundResource(R.drawable.circle_4_current_day)
                        } else if (calendarCell.type == FERTILITY_PREDICTED || calendarCell.type == FERTILITY_FUTURE ||
                            calendarCell.type == OVULATION_PREDICTED || calendarCell.type == OVULATION_FUTURE
                        ) {
                            itemView.day_iv.setBackgroundResource(R.drawable.circle_3_current_day)
                        } else {
                            itemView.day_iv.setBackgroundResource(R.drawable.circle_1)
                        }
                    }


                itemView.setOnClickListener {
                    Log.d("CalendarRecyclerAdapter", "calendarCell.type = " + calendarCell.type)
                    listener(calendarCell)
                }
            } else {
                itemView.day_date_tv.text = ""
            }

        }
    }


}