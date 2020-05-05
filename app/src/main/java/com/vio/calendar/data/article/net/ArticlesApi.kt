package com.vio.calendar.data.article.net

import com.vio.calendar.data.article.model.*
import retrofit2.Call
import retrofit2.http.*

@Deprecated("12.09.2019 CHANGED TO FIREBASE STORAGE")
interface ArticlesApi {

    @GET("posts/lang/{code}")
    fun getArticles(@Path("code") languageCode: String): Call<List<Article>?>

    @GET("search/article")
    fun searchArticle()

    @GET("likes/post.id/{article_id}")
    fun getLikes(
        @Path("article_id") articleId: String
    ): Call<List<LikesResponseItem>>

    @POST("likes/post.id/{article_id}/unlike")
    fun unlike(
        @Path("article_id") articleId: String,
        @Query("token") api_key: String): Call<Any>

    @POST("likes/post.id/{article_id}/like")
    fun like(
        @Path("article_id") articleId: String,
        @Query("token") api_key: String): Call<Any>

    @GET("likes/post.id/{article_id}/count")
        fun getLikesCount(@Path("article_id") articleId: String): Call<LikesResponseCount>

    @POST("comments/post.id/{article_id}/create")
    fun sendComment(@Path("article_id") articleId: String, @Query("token") api_key: String, @Body comment: CommentSend): Call<Any>

    @GET("comments/post.id/{article_id}")
    fun getComment(@Path("article_id") articleId: String): Call<List<Comment>?>
}