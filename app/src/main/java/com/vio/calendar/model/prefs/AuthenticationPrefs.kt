package com.vio.calendar.model.prefs

import android.preference.PreferenceManager
import com.vio.calendar.app.CalendarApplication

object AuthenticationPrefs {

    private const val KEY_AUTH_TOKEN = "KEY_AUTH_TOKEN"

    private const val KEY_USERNAME = "KEY_USERNAME"


    private fun sharedPrefs() = PreferenceManager.getDefaultSharedPreferences(CalendarApplication.getAppContext())
    private fun getAuthToken(): String = sharedPrefs().getString(KEY_AUTH_TOKEN, "")

    fun isAuthenticated() = !getAuthToken().isBlank()

    fun saveUsername(username: String) {
        val editor = sharedPrefs().edit()
        editor.putString(KEY_USERNAME, username).apply()
    }

    fun getUsername(): String = sharedPrefs().getString(KEY_USERNAME, "w00tze")

    fun clearUsername() = sharedPrefs().edit().remove(KEY_USERNAME).apply()
}