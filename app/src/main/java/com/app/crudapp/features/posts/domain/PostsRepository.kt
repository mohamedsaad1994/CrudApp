package com.app.crudapp.features.posts.domain

import com.app.crudapp.features.posts.data.models.PostsListResponse
import com.app.crudapp.features.posts.data.models.PostsListResponseItem
import kotlinx.coroutines.flow.Flow

interface PostsRepository {
    suspend fun getPostsFromServer(): Flow<PostsListResponse>
    suspend fun getPostsFromLocal(): Flow<List<PostsListResponseItem>>

//    suspend fun getPostsFromLocalNeedsUpdate(): Flow<List<PostsListResponseItem>>
//    suspend fun getPostsFromLocalNeedsDelete(): Flow<List<PostsListResponseItem>>
//    suspend fun getPostsFromLocalNeedsCreate(): Flow<List<PostsListResponseItem>>
    suspend fun getPostsFromLocalNeedsToSync(): Flow<List<PostsListResponseItem>>
    suspend fun savePostsInLocal(list: List<PostsListResponseItem>)
    suspend fun addPostLocal(item: PostsListResponseItem): Flow<Long>
    suspend fun addPostRemote(item: PostsListResponseItem): Flow<PostsListResponseItem>
    suspend fun updatePostLocal(item: PostsListResponseItem)
    suspend fun markPostAsDeleted(item: PostsListResponseItem)
    suspend fun updatePostLocalAsSynced(item: PostsListResponseItem)
    suspend fun updatePostRemote(
        item: PostsListResponseItem
    ): Flow<PostsListResponseItem>

    suspend fun deletePostLocal(item: PostsListResponseItem)
    suspend fun deletePostRemote(
        id: Long
    ): Flow<PostsListResponseItem>
}
