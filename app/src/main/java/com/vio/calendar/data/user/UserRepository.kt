package com.vio.calendar.data.user

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vio.calendar.PreferenceHelper
import com.vio.calendar.data.user.model.*
import com.vio.calendar.data.user.net.UserRetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Deprecated("UNUSED")
class UserRepository(context: Context) {

    private val retrofitClient = UserRetrofitClient()
    private val prefs = PreferenceHelper.defaultPrefs(context.applicationContext)


    fun getToken(user: User): String? {
        var token: String? = null
        retrofitClient.getToken(user).enqueue(object : Callback<TokenResponse> {
            override fun onFailure(call: Call<TokenResponse>, t: Throwable) {
                Log.d("UserRepository", "Failure")
            }
            override fun onResponse(call: Call<TokenResponse>, response: Response<TokenResponse>) {
                token = response.body()?.token
                Log.d("UserRepository", token)
                prefs.edit().putString("token", token).apply()
                getUserId(token!!)
            }
        })
        return token
    }

    fun getUserId(token: String): String? {
        var id: String? = null
        retrofitClient.getUserId(token).enqueue(object: Callback<UserFetchResponse> {
            override fun onFailure(call: Call<UserFetchResponse>, t: Throwable) {
                Log.d("UserRepository", "getUserId: failure")
            }

            override fun onResponse(call: Call<UserFetchResponse>, response: Response<UserFetchResponse>) {
                id = response.body()?.record?.id
                Log.d("UserRepistory", id)
                prefs.edit().putString("userid", id).apply()
            }
        })

        return id
    }

    fun updateUser(token: String, userData: UserData) {
        retrofitClient.updateUser(token, userData).enqueue(object: Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.d("UserRepistory", "updateuser+")
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.d("UserRepistory", "updateuser+")
            }
        })
    }

    fun getSettings(): LiveData<List<Settings>?> {

        val data = MutableLiveData<List<Settings>>()

        retrofitClient.getSettings().enqueue(object: Callback<List<Settings>?> {
            override fun onFailure(call: Call<List<Settings>?>, t: Throwable) {
                data.value = null
                Log.d("UserRepository", "failed to fetch settings")
            }

            override fun onResponse(call: Call<List<Settings>?>, response: Response<List<Settings>?>) {

                Log.d("UserRepository", "fetched settings [${response.body()?.get(0)}]")

                val splashId = response.body()?.get(0)?.idSplash
                val screensId = response.body()?.get(0)?.idScreens
                val topId = response.body()?.get(0)?.idTop
                val counter = response.body()?.get(0)?.screenCounter
                val inter = response.body()?.get(0)?.inter
                val splash = response.body()?.get(0)?.splash
                val top = response.body()?.get(0)?.top

                if (inter != null) {
                    prefs.edit().putInt("inter", inter).apply()
                }

                if (splash != null) {
                    prefs.edit().putInt("splash", splash).apply()
                }

                if (top != null) {
                    prefs.edit().putInt("top", top).apply()
                }

                if (counter != null) {
                    prefs.edit().putInt("counter", counter).apply()
                }

                prefs.edit().putString("splashId", splashId).apply()
                prefs.edit().putString("screensId", screensId).apply()
                prefs.edit().putString("topId", topId).apply()
            }

        })

        return data
    }

}