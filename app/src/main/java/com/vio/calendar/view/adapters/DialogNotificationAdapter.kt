package com.vio.calendar.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.vio.calendar.R
import com.vio.calendar.model.dialog.NotificationItem


@Deprecated("UNUSED")
class DialogNotificationAdapter(context: Context, private var list: List<NotificationItem>) :
    ArrayAdapter<NotificationItem>(context, 0, list), View.OnClickListener {


    override fun onClick(v: View?) {

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_notif, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val notificationItem = list[position]

        holder.image?.setImageResource(notificationItem.image)
        holder.text?.text = notificationItem.title
        holder.checkBox?.isChecked = notificationItem.isEnabled


        return view
    }

    private class ViewHolder(row: View?) {
        var image: ImageView? = null
        var text: TextView? = null
        var checkBox: CheckBox? = null

        init {
            this.image = row?.findViewById(R.id.imageN)
            this.text = row?.findViewById(R.id.textN)
            this.checkBox = row?.findViewById(R.id.checkBoxN)
        }
    }
}