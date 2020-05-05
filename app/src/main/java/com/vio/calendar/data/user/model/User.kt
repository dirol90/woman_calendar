package com.vio.calendar.data.user.model

import com.google.gson.annotations.SerializedName

@Deprecated("UNUSED")
data class User(
    @SerializedName("login")
    var name: String,
    @SerializedName("pass")
    var pass: String,
    @SerializedName("data")
    var userData: UserData
)