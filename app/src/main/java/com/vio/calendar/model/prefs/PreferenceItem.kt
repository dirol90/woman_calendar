package com.vio.calendar.model.prefs

data class PreferenceItem (
    val key: String,
    val title: String,
    val summary: String,
    val image: Int,
    var value: Int
)