package com.vio.calendar.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vio.calendar.R
import kotlinx.android.synthetic.main.fragment_number_picker_menstrual.*

class NumberPickerFragmentMenstrual: Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_number_picker_menstrual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        numberPicker.scrollTo(5)
    }


    fun value(): Int = numberPicker.getCurrentItem().toInt()


}