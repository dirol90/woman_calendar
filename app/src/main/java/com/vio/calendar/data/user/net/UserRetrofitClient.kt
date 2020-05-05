package com.vio.calendar.data.user.net

import com.vio.calendar.data.user.model.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Deprecated("UNUSED")
class UserRetrofitClient {

    private val userApi: UserApi

    companion object {
        private const val API_BASE_URL = "http://134.209.23.52/api/v1/"
    }

    init {
        val builder = OkHttpClient.Builder()
        val okHttpClient = builder.build()
        val retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        userApi = retrofit.create(UserApi::class.java)
    }

    fun getToken(user: User): Call<TokenResponse> {
        return userApi.getToken(user)
    }

    fun getUserId(token: String): Call<UserFetchResponse> {
        return userApi.getUserId(token)
    }

    fun updateUser(token: String, userData: UserData): Call<Any> {
        return userApi.updateUser(token, userData)
    }

    fun getSettings(): Call<List<Settings>> {
        return userApi.getSettings()
    }
  }