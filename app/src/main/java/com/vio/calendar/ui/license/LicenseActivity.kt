package com.vio.calendar.ui.license

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.vio.calendar.R
import com.vio.calendar.view.activities.StepsActivity
import kotlinx.android.synthetic.main.activity_license.*



class LicenseActivity : LocalizationActivity() {

    private lateinit var adapter: LicenseRecyclerAdapter
    private val licenseContent = ArrayList<LicenseItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_license)

        hideBottomPanel()
        for (itemString in resources.getStringArray(R.array.license_menu)) {
            licenseContent.add(LicenseItem(false, itemString))
        }

        adapter = LicenseRecyclerAdapter(licenseContent) {
            it.isChecked = true
            if (adapter.isAllChecked()) {
                licenseAgree()
            }
        }

        recyclerViewLicense.layoutManager = LinearLayoutManager(this)
        recyclerViewLicense.adapter = adapter

        button_accept_all.setOnClickListener {
            adapter.checkAll()
            licenseAgree()
        }

        button_next.setOnClickListener {
            val intent = Intent(this, StepsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun licenseAgree() {
        button_next.isClickable = true
        button_next.setTextColor(ContextCompat.getColor(this, android.R.color.white))
        button_next.setBackgroundResource(R.drawable.button_pink)
        button_accept_all.visibility = View.GONE
    }

    private fun hideBottomPanel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
    }

}