package com.vio.calendar.model.article

import com.google.gson.annotations.SerializedName

data class LikesCount(
    @SerializedName("count")
    val count: Int
)