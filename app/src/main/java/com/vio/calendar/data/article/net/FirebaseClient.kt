package com.vio.calendar.data.article.net

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.vio.calendar.data.article.model.Article

open class FirebaseClient: LiveData<List<Article>>() {

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()
    var articles: MutableLiveData<List<Article>?> = MutableLiveData()

    init {
        if (database == null) {
            database.setPersistenceEnabled(true)
        }
    }


    fun getArticlesByLang(lang: String): MutableLiveData<List<Article>?> {
        val ref = database.getReference("Objects/$lang/article/")
        ref.keepSynced(true)
        articles.value = mutableListOf()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var articleList = mutableListOf<Article>()
                dataSnapshot.children.forEach { obj ->
                    val a = obj.getValue(Article::class.java)
                    if (a != null) {
                        articleList.add(a)
                    }
                }
                articles.value = articleList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i("FIREBASE", error.message)
            }
        })
        return articles
    }

    fun changeArticleLikeValue(lang: String, _id: String, value: String) {
        val ref = database.getReference("Objects/$lang/article/${_id}/likes")

        ref.setValue(value)
    }
}
