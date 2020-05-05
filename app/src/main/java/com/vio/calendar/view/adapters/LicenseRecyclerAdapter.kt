package com.vio.calendar.ui.license

import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vio.calendar.R
import com.vio.calendar.inflate
import kotlinx.android.synthetic.main.item_license.view.*

class LicenseRecyclerAdapter(
    private val items: ArrayList<LicenseItem>,
    private val listener: (LicenseItem) -> Unit
): RecyclerView.Adapter<LicenseRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = parent.inflate(R.layout.item_license)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindItem(item, listener)
    }

    override fun getItemCount() = items.size

    fun isAllChecked(): Boolean {
        var temp = true
        for (item in items) {
            if (!item.isChecked) temp = false
        }
        return temp
    }

    fun checkAll() {
        for (item in items) item.isChecked = true
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindItem(item: LicenseItem, listener: (LicenseItem) -> Unit) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                itemView.textLicense.text = Html.fromHtml(item.text, Html.FROM_HTML_MODE_COMPACT)
            } else {
                itemView.textLicense.text = Html.fromHtml(item.text)
                Linkify.addLinks(itemView.textLicense, Linkify.ALL);
                itemView.textLicense.movementMethod = LinkMovementMethod.getInstance();
            }

            itemView.radioBtnLicense.isChecked = item.isChecked

            itemView.radioBtnLicense.setOnCheckedChangeListener { _, _ ->
                listener(item)
            }
        }
    }
}