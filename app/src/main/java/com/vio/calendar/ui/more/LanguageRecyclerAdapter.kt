package com.vio.calendar.ui.more

import android.content.SharedPreferences
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.inflate
import com.vio.calendar.model.dialog.LanguageItem
import kotlinx.android.synthetic.main.item_language.view.*
import com.vio.calendar.ui.main.MainActivity
import com.vio.calendar.utils.LocaleUtils
import java.util.*
import android.os.Build

class LanguageRecyclerAdapter(var prefs: SharedPreferences): RecyclerView.Adapter<LanguageRecyclerAdapter.ViewHolder>() {

    private var languages: MutableList<LanguageItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.item_language))
    }

    override fun getItemCount() = languages.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = languages[position]
        holder.bind(item)
    }

    fun setLanguages(languages: List<LanguageItem>) {
        this.languages.clear()
        this.languages.addAll(languages)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(languageItem: LanguageItem) {

            itemView.imageLanguage.setImageResource(languageItem.image)
            itemView.textN.text = languageItem.name

            itemView.setOnClickListener{
                CalendarApplication.prefs!!.edit().putString("lang", languageItem.code.toLowerCase()).apply()
                CalendarApplication.code = languageItem.code.toLowerCase()
                LocaleUtils.setLocale(itemView.context, languageItem.code.toLowerCase())

                val res = itemView.context.resources
                val dm = res.displayMetrics
                val conf = res.configuration
                conf.setLocale(Locale(languageItem.code.toLowerCase()))

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                    itemView.context.createConfigurationContext(conf)
//                } else {
                    res
                        .updateConfiguration(conf, dm)
//                }

                MainActivity.reload(itemView.context)
            }
        }
    }
}