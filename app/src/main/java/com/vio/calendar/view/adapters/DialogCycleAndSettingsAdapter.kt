package com.vio.calendar.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.vio.calendar.R
import com.vio.calendar.model.dialog.NotificationItem

@Deprecated("UNUSED")
class DialogCycleAndSettingsAdapter(context: Context, list: List<NotificationItem>) :
    ArrayAdapter<NotificationItem>(context, 0, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val vh: ViewHolder
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_language, parent, false)
            vh = ViewHolder(convertView!!)
            convertView!!.tag = vh
        } else
            vh = convertView!!.tag as ViewHolder

        val option = getItem(position)

        vh.imageCycleSet.setImageResource(option.image)
        vh.text.text = option!!.title

        return convertView
    }

    private class ViewHolder(v: View) {
        internal var imageCycleSet: ImageView = v.findViewById(R.id.imageN)
        internal var text: TextView = v.findViewById(R.id.textN)

    }
}