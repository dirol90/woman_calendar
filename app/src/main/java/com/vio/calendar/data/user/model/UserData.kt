package com.vio.calendar.data.user.model

import com.google.gson.annotations.SerializedName

@Deprecated("UNUSED")
data class UserData(
    @SerializedName("name")
    var name: String = "",
    @SerializedName("color")
    val color: String = "#51d0de"
)