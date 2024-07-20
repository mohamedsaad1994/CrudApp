package com.app.crudapp.features.posts.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.app.crudapp.features.posts.data.models.PostsListResponseItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPostsList(items: List<PostsListResponseItem>)

    @Query("SELECT * FROM PostsListResponseItem WHERE isDeleted IS NULL OR isDeleted IS 0")
    fun getAllPosts(): Flow<List<PostsListResponseItem>>

//    @Query("SELECT * FROM PostsListResponseItem WHERE synced=0 AND userId=11")//assume posts with user id ==11 are new posts
//    fun getPostsNeedCreate(): Flow<List<PostsListResponseItem>>
//    @Query("SELECT * FROM PostsListResponseItem WHERE synced=0 AND userId!=11")//assume posts with user id !=11 are already exist in API but need update
//    fun getPostsNeedUpdate(): Flow<List<PostsListResponseItem>>
//    @Query("SELECT * FROM PostsListResponseItem WHERE isDeleted=1 ")
//    fun getPostsNeedDelete(): Flow<List<PostsListResponseItem>>

    @Query("SELECT * FROM PostsListResponseItem WHERE synced=0 ")
    fun getPostsNeedToSync(): Flow<List<PostsListResponseItem>>

    @Insert
    suspend fun insertPost(postItem: PostsListResponseItem): Long

    @Delete
    suspend fun deletePost(postItem: PostsListResponseItem)


    @Update
    suspend fun updatePost(postItem: PostsListResponseItem): Int
}