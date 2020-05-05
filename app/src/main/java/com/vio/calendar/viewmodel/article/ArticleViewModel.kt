package com.vio.calendar.viewmodel.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.vio.calendar.data.article.ArticleRepository
import com.vio.calendar.data.article.ArticleRepositoryImpl
import com.vio.calendar.data.article.model.Article
import com.vio.calendar.data.article.model.Comment
@Deprecated("12.09.2019 CHANGED TO FIREBASE STORAGE")
class ArticleViewModel(private val repository: ArticleRepository = ArticleRepositoryImpl()): ViewModel() {

//    private var allArticles = mutableListOf<>()<List<Article>?>()

    init {
//        getAllArticles()
    }

//    fun getSavedArticles() = allArticles

    private fun getAllArticles(): LiveData<List<Article>?> {
        return repository.getAllArticles()
    }

    fun getArticles(code: String): LiveData<List<Article>?> {
        return repository.getArticles(code)
    }

    fun deleteSavedArticles(article: Article) {
        repository.deleteArticle(article)
    }

    fun deleteAllSavedArticles() {
        repository.deleteAll()
    }

    fun getComments(article: Article): LiveData<List<Comment>?> {
        return repository.getComments(article)
    }

}