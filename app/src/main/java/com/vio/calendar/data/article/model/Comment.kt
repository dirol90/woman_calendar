package com.vio.calendar.data.article.model

import com.google.gson.annotations.SerializedName
import com.vio.calendar.data.user.model.UserData
import java.util.*

@Deprecated("12.09.2019 CHANGED TO FIREBASE STORAGE, CHANGED APP DESIGN")
data class Comment(
    @SerializedName("_id")
    var id: String,
    @SerializedName("user_data")
    var userData: UserData,
    @SerializedName("content")
    var content: String,
    @SerializedName("created_at")
    var createdAt: Date
)