package com.vio.calendar.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.vio.calendar.R
import kotlinx.android.synthetic.main.day_item_row.view.*

class DayWeekAdapter(context: Context, list: ArrayList<String>) : BaseAdapter() {
    private var namesDayWeek = list
    var context: Context? = context

    override fun getCount(): Int {
        return namesDayWeek.size
    }

    override fun getItem(position: Int): Any {
        return namesDayWeek[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val name = this.namesDayWeek[position]
        val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflator.inflate(R.layout.day_item_row, null)
        view.dayName.text = name
        return view
    }
}
