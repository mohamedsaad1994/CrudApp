package com.app.crudapp.core.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.crudapp.features.posts.data.local.PostsDao
import com.app.crudapp.features.posts.data.models.PostsListResponseItem

@Database(
    entities = [PostsListResponseItem::class],
    version = 1
)
abstract class PostsDb : RoomDatabase() {
    abstract fun getPostsDao(): PostsDao

    companion object {
        private val LOCK = Any()
        private const val DATABASE_NAME = "Posts_8.Db"

        @Volatile
        private var instance: PostsDb? = null

        operator fun invoke(context: Context): PostsDb {
            return instance ?: synchronized(LOCK) {
                instance ?: buildDb(context).also {
                    instance = it
                }
            }
        }

        private fun buildDb(context: Context) =
            Room.databaseBuilder(context.applicationContext, PostsDb::class.java, DATABASE_NAME)
                .build()

    }

}