package com.vio.calendar.data.article.model

import com.google.gson.annotations.SerializedName

@Deprecated("12.09.2019 CHANGED TO FIREBASE STORAGE, CHANGED APP DESIGN")
class LikesResponseCount {
    @SerializedName("count")
    var likesCount: Int = 3
}