package com.vio.calendar.ui.more

import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.vio.calendar.R
import com.vio.calendar.model.dialog.LanguageItem
import kotlinx.android.synthetic.main.activity_language.*

@Deprecated("UNUSED")
class LanguageActivity : LocalizationActivity() {

    private val languages = ArrayList<LanguageItem>()
    private lateinit var adapter: LanguageRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        toolbarLanguage.setNavigationOnClickListener {
                _ -> onBackPressed()
        }

        languages.add(LanguageItem(R.drawable.ic_english, "en", "English"))
        languages.add(LanguageItem(R.drawable.ic_spain, "es", "Spain"))
        languages.add(LanguageItem(R.drawable.ic_russia, "ru", "Русский"))

//        adapter = LanguageRecyclerAdapter(languages) {it -> setLanguage(it.code)}


        recyclerLanguage.layoutManager = LinearLayoutManager(this)
        recyclerLanguage.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerLanguage.adapter = adapter
    }
}
