package com.vio.calendar.data.article.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//@Entity(tableName = "article")
data class Article(
//    @PrimaryKey
//    @SerializedName("_id")
//    @Expose
    var _id: String? = "",
//    @SerializedName("title")
    var title: String = "",
//    @SerializedName("short_description")
    var short_description: String ="",
//    @SerializedName("content")
    var content: String ="",
//    @SerializedName("created_at")
    var created_at: Long = -1,
//    @SerializedName("picture_url")
    var picture_url: String ="",
//    @SerializedName("view_count")
    var view_count: String = "",
//    @SerializedName("likes")
    var likes: String = "",
//    @SerializedName("is_vip")
    var is_vip: Boolean = false
) {
    fun setIs_vip (is_vip: Boolean){
        this.is_vip = is_vip
    }

    fun getIs_vip(): Boolean{
        return is_vip
    }
}