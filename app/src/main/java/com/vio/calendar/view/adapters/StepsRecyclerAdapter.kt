package com.vio.calendar.view.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vio.calendar.R
import com.vio.calendar.inflate
import com.vio.calendar.model.date.CalendarCell
import kotlinx.android.synthetic.main.steps_day_item_row.view.*

@Deprecated("UNUSED")
class StepsRecyclerAdapter(val days: ArrayList<CalendarCell>, private val firstDay: Int, private val listener: (CalendarCell) -> Unit) :
    RecyclerView.Adapter<StepsRecyclerAdapter.DayViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val inflatedView = parent.inflate(R.layout.steps_day_item_row)
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

            if (position + 1 >= firstDay) {

                itemView.stepsDayButton.alpha = 1.0f
                itemView.stepsDayButton.setOnClickListener {
                    listener(calendarCell)
                }
                itemView.stepsDayTitle.text = calendarCell.day.toString()

            } else {
                itemView.stepsDayTitle.text = ""
                itemView.stepsDayButton.alpha = 0.0f
            }

        }
    }


}
