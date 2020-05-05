package com.vio.calendar.ui.review

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.vio.calendar.R
import com.vio.calendar.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_articles.view.*
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.widget.Button
import java.lang.Exception


class ReviewFragment (var prefs: SharedPreferences): Fragment()  {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_for_review, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Review", (activity as MainActivity).prefs.getString("token", "token"))

        view.findViewById<ImageButton>(R.id.exit_review_window).setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        view.findViewById<Button>(R.id.no_review_btn).setOnClickListener {
            fragmentManager!!.popBackStack()
        }

        view.findViewById<Button>(R.id.yes_review_btn).setOnClickListener {
            prefs.edit().putBoolean("AppReviewPressed", true).apply()
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("market://details?id=com.vio.calendar")
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            fragmentManager!!.popBackStack()
        }
    }

}
