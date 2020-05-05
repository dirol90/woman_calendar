package com.vio.calendar.data.article.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.vio.calendar.data.article.model.Article

@Deprecated("12.09.2019 CHANGED TO FIREBASE STORAGE")
@Dao
interface ArticleDao {
    @Query("SELECT * FROM article")
    fun getAll(): LiveData<List<Article>?>

    @Insert(onConflict = REPLACE)
    fun insert(article: Article)

    @Query("DELETE FROM article WHERE id = :id")
    fun delete(id: String?)

    @Query("DELETE FROM article")
    fun deleteAll()

    @Update
    fun update(article: Article)
}