package com.vio.calendar.data.user.net

import com.vio.calendar.data.user.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

@Deprecated("UNUSED")
interface UserApi {

    @POST("users/sign-up")
    fun getToken(@Body user: User): Call<TokenResponse>

    @GET("users/current/fetch")
    fun getUserId(@Query("token") token: String): Call<UserFetchResponse>

    @POST("users/current/update")
    fun updateUser(@Query("token") token: String, @Body userData: UserData): Call<Any>

    @GET("settings")
    fun getSettings(): Call<List<Settings>>
}