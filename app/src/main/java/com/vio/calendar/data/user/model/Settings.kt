package com.vio.calendar.data.user.model

import com.google.gson.annotations.SerializedName

@Deprecated("UNUSED")
data class Settings (
    @SerializedName("id_splash")
    var idSplash: String = "ca-app-pub-1890073619173649/2395311128",
    @SerializedName("id_screens")
    var idScreens: String = "ca-app-pub-1890073619173649/8078882648",
    @SerializedName("id_top")
    var idTop: String = "ca-app-pub-1890073619173649/4909793826",
    @SerializedName("screens_counter")
    var screenCounter: Int = 5,
    @SerializedName("splash")
    var splash: Int = 1,
    @SerializedName("inter")
    var inter: Int = 1,
    @SerializedName("top")
    var top: Int = 1
)