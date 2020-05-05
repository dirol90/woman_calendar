package com.vio.calendar.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.vio.calendar.R
import com.vio.calendar.model.dialog.CycleItem

@Deprecated("UNUSED")
class DialogCycleAdapter(context: Context, private var list: List<CycleItem>) :
    ArrayAdapter<CycleItem>(context, 0, list){


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_prefs_cycle, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val cycleItem = list[position]

        holder.image?.setImageResource(cycleItem.image)
        holder.title?.text = cycleItem.title
        holder.summary?.text = cycleItem.summary


        return view
    }

    private class ViewHolder(row: View?) {
        var image: ImageView? = null
        var title: TextView? = null
        var value: TextView? = null
        var summary: TextView? = null

        init {
            this.image = row?.findViewById(R.id.image)
            this.title = row?.findViewById(R.id.title)
            this.value = row?.findViewById(R.id.value)
            this.summary = row?.findViewById(R.id.summary)
        }
    }
}