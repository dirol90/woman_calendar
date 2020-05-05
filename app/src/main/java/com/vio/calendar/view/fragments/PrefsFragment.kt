package com.vio.calendar.view.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.super_rabbit.wheel_picker.OnValueChangeListener
import com.super_rabbit.wheel_picker.WheelPicker
import com.vio.calendar.PreferenceHelper.defaultPrefs
import com.vio.calendar.R
import com.vio.calendar.app.CalendarApplication
import com.vio.calendar.db.PeriodicalDatabase
import com.vio.calendar.model.prefs.PreferenceItem
import com.vio.calendar.ui.main.MainActivity
import com.vio.calendar.ui.more.LanguageRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_prefsa_new.*
import kotlinx.android.synthetic.main.fragment_prefsa_new.view.*
import kotlinx.android.synthetic.main.item_profile.userName
import java.lang.Exception


class PrefsFragment : Fragment() {

    private lateinit var prefs: SharedPreferences
    val list = ArrayList<PreferenceItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prefsa_new, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        prefs = defaultPrefs(context.applicationContext)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userName.text = prefs.getString("user_name", "no user name")

        edit_iv.setOnClickListener {
            userName.visibility = View.INVISIBLE
            userNameEditText.visibility = View.VISIBLE
            userNameEditText.setText(userName.text)
            edit_iv.setImageDrawable(context!!.resources.getDrawable(R.drawable.apply_img))
            edit_iv.setOnClickListener {
                if (!userNameEditText.text.isEmpty()) {
                    prefs.edit().putString("user_name", userNameEditText.text.toString()).apply()
                    userName.text = userNameEditText.text.toString()
                }
                edit_iv.setImageDrawable(context!!.resources.getDrawable(R.drawable.edit_img))
                userName.visibility = View.VISIBLE
                userNameEditText.visibility = View.GONE
                edit_iv.setOnClickListener {
                    userName.visibility = View.INVISIBLE
                    userNameEditText.visibility = View.VISIBLE
                    userNameEditText.setText(userName.text)
                    edit_iv.setImageDrawable(context!!.resources.getDrawable(R.drawable.apply_img))
                    edit_iv.setOnClickListener {
                        if (!userNameEditText.text.isEmpty()) {
                            prefs.edit().putString("user_name", userNameEditText.text.toString()).apply()
                            userName.text = userNameEditText.text.toString()
                        }
                        edit_iv.setImageDrawable(context!!.resources.getDrawable(R.drawable.edit_img))
                        userName.visibility = View.VISIBLE
                        userNameEditText.visibility = View.GONE

                    }
                }
            }
        }
//
//        cardUserName.setOnClickListener {
//            (activity as MainActivity).showEditTextDialog()
//        }

        switch_m_end.isChecked = prefs.getBoolean("notification_m_end", false)
        switch_m_start.isChecked = prefs.getBoolean("notification_m_start", true)
        switch_ovulation.isChecked = prefs.getBoolean("notification_ovulation", true)


        val adapter = LanguageRecyclerAdapter(prefs)
        view.recyclerLanguageNew.layoutManager = LinearLayoutManager(context)
        view.recyclerLanguageNew.adapter = adapter
        adapter.setLanguages(CalendarApplication.languages)


        var lastnumberPickerValue = -1
        numberPicker.scrollToValue(CalendarApplication.getDatabaseHelper().getOption("period_length", 3).toString())
        numberPicker.setOnValueChangeListener(object : OnValueChangeListener{
            override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) {
                lastnumberPickerValue = newVal.toInt()
                save_period_value.visibility = View.VISIBLE
            }
        })

        var lastnumberPickerCycleLengthValue = -1
        numberPickerCycleLength.scrollToValue(CalendarApplication.getDatabaseHelper().getOption("maximum_cycle_length", 28).toString())
        numberPickerCycleLength.setOnValueChangeListener(object : OnValueChangeListener{
            override fun onValueChange(picker: WheelPicker, oldVal: String, newVal: String) {
                lastnumberPickerCycleLengthValue = newVal.toInt()
                save_cycle_value.visibility = View.VISIBLE
            }
        })

        save_cycle_value.setOnClickListener {
            prefs.edit().putInt("maximum_cycle_length", lastnumberPickerCycleLengthValue).apply()
            save_cycle_value.visibility = View.GONE
            CalendarApplication.getDatabaseHelper().setOption("maximum_cycle_length", lastnumberPickerCycleLengthValue)

            var period =3
            var cycle = 28
            try {
                period = CalendarApplication.prefs!!.getInt("period_length", 3)
                cycle = CalendarApplication.prefs!!.getInt("maximum_cycle_length", 28)
            }catch (e: Exception){
                e.printStackTrace()
            }


            CalendarApplication.getDatabaseHelper().updateDataInDatabase(cycle, period)
        }

        save_period_value.setOnClickListener {
            prefs.edit().putInt("period_length", lastnumberPickerValue).apply()
            save_period_value.visibility = View.GONE
            CalendarApplication.getDatabaseHelper().setOption("period_length", lastnumberPickerValue)

            var period =3
            var cycle = 28
            try {
                period = CalendarApplication.prefs!!.getInt("period_length", 3)
                cycle = CalendarApplication.prefs!!.getInt("maximum_cycle_length", 28)
            }catch (e: Exception){
                e.printStackTrace()
            }

            CalendarApplication.getDatabaseHelper().updateDataInDatabase(cycle, period)
        }


        switch_m_end.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notification_m_end", isChecked).apply()
        }
        switch_m_start.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notification_m_start", isChecked).apply()
        }
        switch_ovulation.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notification_ovulation", isChecked).apply()
        }
    }

}