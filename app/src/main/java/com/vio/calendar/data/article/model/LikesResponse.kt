package com.vio.calendar.data.article.model

import com.google.gson.annotations.SerializedName

@Deprecated("12.09.2019 CHANGED TO FIREBASE STORAGE, CHANGED APP DESIGN")
data class LikesResponseItem (
    @SerializedName("user_id")
    var userId: String
)