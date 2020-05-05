package com.vio.calendar.data.article.net

import android.util.Log
import com.google.gson.GsonBuilder
import com.vio.calendar.data.article.model.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Deprecated("12.09.2019 CHANGED TO FIREBASE STORAGE")
class RetrofitClient {

    private val articlesApi: ArticlesApi

    companion object {
        private const val API_BASE_URL = "http://134.209.23.52/api/v1/"
    }

    init {
        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:sss'Z'").create()
        val builder = OkHttpClient.Builder()
        val okHttpClient = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
        articlesApi = retrofit.create(ArticlesApi::class.java)
    }

    fun getArticles(code: String): Call<List<Article>?> {
        return articlesApi.getArticles(code)
    }

    fun getComments(article: Article): Call<List<Comment>?> {
        return articlesApi.getComment(article._id!!)
    }

    fun getLikesCount(article: Article): Call<LikesResponseCount> {
        Log.d("getLikesCount", "retrofit - article ${article._id}")
        return articlesApi.getLikesCount(article._id!!)
    }

    fun sendComment(articleId: String, api_key: String, comment: CommentSend): Call<Any> {
        return articlesApi.sendComment(articleId, api_key, comment)
    }

    fun like(article: Article,  token: String): Call<Any> {
        return articlesApi.like(article._id!!, token)
    }

    fun unlike(article: Article,  token: String): Call<Any> {
        return articlesApi.unlike(article._id!!, token)
    }

    fun getLikes(article: Article): Call<List<LikesResponseItem>> {
        return articlesApi.getLikes(article._id!!)
    }
}