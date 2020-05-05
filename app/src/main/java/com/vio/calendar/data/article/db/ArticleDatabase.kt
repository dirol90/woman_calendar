package com.vio.calendar.data.article.db

import android.app.Application
import androidx.room.Database
import androidx.room.RoomDatabase
import com.vio.calendar.data.article.model.Article

@Database(entities = [Article::class], version = 1)
@Deprecated("12.09.2019 CHANGED TO FIREBASE STORAGE")
abstract class ArticleDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
//        private val lock = Any()
        private const val DB_NAME = "article_database"
        private var INSTANCE: ArticleDatabase? = null

        fun getInstance(application: Application) = INSTANCE
            /*synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE =
                        Room.databaseBuilder(application, ArticleDatabase::class.java,
                            DB_NAME
                        )
                            .allowMainThreadQueries()
                            .build()
                }
            }
            return INSTANCE!!*/

    }
}