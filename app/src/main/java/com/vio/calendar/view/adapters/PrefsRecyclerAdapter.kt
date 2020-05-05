package com.vio.calendar.view.adapters

import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vio.calendar.R
import com.vio.calendar.inflate
import com.vio.calendar.model.prefs.PreferenceItem
import kotlinx.android.synthetic.main.item_prefs_cycle.view.*
import kotlinx.android.synthetic.main.item_profile.view.*


@Deprecated("UNUSED")
class PrefsRecyclerAdapter(
    private val prefsList: ArrayList<PreferenceItem>,
    private val prefs: SharedPreferences,
    private val listenerCycle: () -> Unit,
    private val listenerLanguage: () -> Unit,
    private val listenerNotification: () -> Unit,
    private val listenerEditText: () -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            0 -> PrefViewHolder(parent.inflate(R.layout.item_prefs_new))
            else -> ViewHolderProfile(parent.inflate(R.layout.item_profile))
        }
    }

    override fun getItemCount() = prefsList.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 1 else 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PrefViewHolder) {
            holder.bind(prefsList[position - 1])
        } else if (holder is ViewHolderProfile) {
            holder.bind()
        }

    }

    inner class PrefViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(preference: PreferenceItem) {
            itemView.image.setImageResource(preference.image)
            itemView.title.text = preference.key

            Log.d("PrefsRecyclerAdapter", "hmm: ${preference.image}")

            when (preference.value) {
                2 -> itemView.setOnClickListener {
                    listenerLanguage()
                }

                1 -> itemView.setOnClickListener {
                    listenerNotification()
                }

                0 -> itemView.setOnClickListener {
                    listenerCycle()
                }
            }
        }

    }

    inner class ViewHolderProfile(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.userName.text = prefs.getString("user_name", "default_name")
            itemView.setOnClickListener {
                listenerEditText()
            }
        }
    }
}