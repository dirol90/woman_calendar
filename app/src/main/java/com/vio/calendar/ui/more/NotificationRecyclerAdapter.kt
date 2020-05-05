package com.vio.calendar.ui.more

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vio.calendar.R
import com.vio.calendar.inflate
import com.vio.calendar.model.dialog.NotificationItem
import kotlinx.android.synthetic.main.item_notfication.view.*

@Deprecated("UNUSED")
class NotificationRecyclerAdapter(val list: ArrayList<NotificationItem>, val listener: (NotificationItem) -> Unit): RecyclerView.Adapter<NotificationRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_notfication))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.bindItem(item, listener)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(item: NotificationItem, listener: (NotificationItem) -> Unit) {
            itemView.titleNS.text = item.title
        }
    }
}