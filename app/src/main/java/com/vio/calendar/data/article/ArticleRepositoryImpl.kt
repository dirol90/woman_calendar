package com.vio.calendar.data.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vio.calendar.data.article.model.*
import com.vio.calendar.data.article.net.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

@Deprecated("12.09.2019 CHANGED TO FIREBASE STORAGE")
class ArticleRepositoryImpl: ArticleRepository {
    override fun deleteAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllArticles(): LiveData<List<Article>?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // private val articleDao: ArticleDao = db.articleDao()
    private val retrofitClient = RetrofitClient()
   // private val allArticles: LiveData<List<Article>>

    init {
       //allArticles = articleDao.getAll()
    }

    //override fun getSavedArticles(): LiveData<List<Article>> = allArticles

    override fun saveArticle(article: Article) {
        thread {
           // articleDao.insert(article)
        }
    }

    override fun deleteArticle(article: Article) {
        thread {
           // articleDao.delete(article.id)
        }
    }

    override fun getLikesCount(article: Article): LiveData<LikesResponseCount?> {

        val data = MutableLiveData<LikesResponseCount>()

        retrofitClient.getLikesCount(article).enqueue(object: Callback<LikesResponseCount?> {
            override fun onFailure(call: Call<LikesResponseCount?>, t: Throwable) {
                data.value = null
                Log.d("ArticleRepository", "getLikesCount - failure")
            }

            override fun onResponse(call: Call<LikesResponseCount?>, response: Response<LikesResponseCount?>) {
                data.value = response.body()
                Log.d("ArticleRepository", "likesCount Response: ${response.body()?.likesCount}")
            }

        })

        return data
    }

    override fun like(article: Article, token: String) {
        retrofitClient.like(article, token).enqueue(object: Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.d("ArticleRepositoryImpl", "failure")
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.d("ArticleRepositoryImpl", "response")
            }

        })
    }

    override fun unlike(article: Article, token: String) {
        retrofitClient.unlike(article, token).enqueue(object: Callback<Any> {
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Log.d("ArticleRepositoryImpl", "unlike failure")
            }

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                Log.d("ArticleRepositoryImpl", "response")
            }

        })
    }



    override fun getComments(article: Article): LiveData<List<Comment>?> {

        val data = MutableLiveData<List<Comment>>()

        retrofitClient.getComments(article).enqueue(object: Callback<List<Comment>?> {
            override fun onFailure(call: Call<List<Comment>?>, t: Throwable) {
                data.value = null
                Log.d("ArticleRepository", "getComments - failure")
            }

            override fun onResponse(call: Call<List<Comment>?>, response: Response<List<Comment>?>) {
                data.value = response.body()
                Log.d("ArticleRepository", "comments Response: ${response.body()}")
            }

        })

        return data
    }



    override fun getArticles(code: String): LiveData<List<Article>?> {

        val data = MutableLiveData<List<Article>>()

        retrofitClient.getArticles(code).enqueue(object: Callback<List<Article>?> {
            override fun onResponse(call: Call<List<Article>?>, response: Response<List<Article>?>) {
                data.value = response.body()
                Log.d("ArticleRepository", "Response: ${response.body()}")
            }

            override fun onFailure(call: Call<List<Article>?>, t: Throwable) {
                data.value = null
                Log.d("ArticleRepository", "getArticles - failure ${t.localizedMessage}")
            }
        })

        return data
    }

    override fun getLikes(article: Article): LiveData<List<LikesResponseItem>?> {

        val data = MutableLiveData<List<LikesResponseItem>>()

        retrofitClient.getLikes(article).enqueue(object: Callback<List<LikesResponseItem>?> {
            override fun onResponse(call: Call<List<LikesResponseItem>?>, response: Response<List<LikesResponseItem>?>) {
                data.value = response.body()
                Log.d("LikesResponseItem", "Response: ${response.body()}")
            }

            override fun onFailure(call: Call<List<LikesResponseItem>?>, t: Throwable) {
                data.value = null
                Log.d("LikesResponseItem", "getArticles - failure ${t.localizedMessage}")
            }
        })

        return data
    }

    override fun sendComment(articleId: String, api_key: String, comment: CommentSend) {


        retrofitClient.sendComment(articleId, api_key, comment).enqueue(object: Callback<Any?> {
            override fun onResponse(call: Call<Any?>, response: Response<Any?>) {
                Log.d("Reso", "reso")
            }

            override fun onFailure(call: Call<Any?>, t: Throwable) {
                Log.d("Reso", "reso")
            }

        })

    }



}