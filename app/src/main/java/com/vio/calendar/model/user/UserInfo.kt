package com.vio.calendar.model.user

import com.google.gson.annotations.SerializedName
import com.vio.calendar.model.date.Date

data class UserInfo (
    @SerializedName("lengthCycle")
    var lengthCycle: Int = 0,
    @SerializedName("lengthMenstrual")
    var lengthMenstrual: Int = 0,
    @SerializedName("dateCycleStart")
    var dateCycleStart: Date = Date(2000, 1, 1),
    @SerializedName("birthDate")
    var birthDate: Int = 1990
)