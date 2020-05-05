package com.vio.calendar.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.vio.calendar.R
import com.vio.calendar.model.dialog.LanguageItem

@Deprecated("UNUSED")
class DialogLanguageAdapter(context: Context, donationOptions: List<LanguageItem>) :
    ArrayAdapter<LanguageItem>(context, 0, donationOptions) {

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

        vh.imageLanguage.setImageResource(option.image)
        vh.text.text = option!!.name

        return convertView
    }

    private class ViewHolder(v: View) {
        internal var imageLanguage: ImageView = v.findViewById(R.id.imageLanguage)
        internal var text: TextView = v.findViewById(R.id.textN)

    }
}