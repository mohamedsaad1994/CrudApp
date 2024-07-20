package com.app.crudapp.features.posts.data

import com.app.crudapp.core.data.PostsDb
import com.app.crudapp.features.posts.data.models.PostsListResponse
import com.app.crudapp.features.posts.data.models.PostsListResponseItem
import com.app.crudapp.features.posts.data.remote.PostsService
import com.app.crudapp.features.posts.domain.PostsRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class PostsRepositoryImpl @Inject constructor(
    private val postsService: PostsService,
    private val db: PostsDb
) : PostsRepository {

    override suspend fun getPostsFromServer(): Flow<PostsListResponse> {
        return flowOf(postsService.getPosts())
    }

    override suspend fun getPostsFromLocal(): Flow<List<PostsListResponseItem>> {
        return db.getPostsDao().getAllPosts()
    }

//    override suspend fun getPostsFromLocalNeedsUpdate(): Flow<List<PostsListResponseItem>> {
//        return db.getPostsDao().getPostsNeedUpdate()
//    }
//
//    override suspend fun getPostsFromLocalNeedsDelete(): Flow<List<PostsListResponseItem>> {
//        return db.getPostsDao().getPostsNeedDelete()
//    }
//
//    override suspend fun getPostsFromLocalNeedsCreate(): Flow<List<PostsListResponseItem>> {
//        return db.getPostsDao().getPostsNeedCreate()
//    }

    override suspend fun getPostsFromLocalNeedsToSync(): Flow<List<PostsListResponseItem>> {
        return db.getPostsDao().getPostsNeedToSync()
    }

    override suspend fun savePostsInLocal(list: List<PostsListResponseItem>) {
        db.getPostsDao().insertPostsList(list)
    }

    override suspend fun addPostLocal(item: PostsListResponseItem): Flow<Long> {
        return flowOf(
            db.getPostsDao()
                .insertPost(item)
        )
    }

    override suspend fun addPostRemote(
        item: PostsListResponseItem
    ): Flow<PostsListResponseItem> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("title", item.title)
        jsonObject.addProperty("body", item.body)
        jsonObject.addProperty("userId", item.userId)
        return flowOf(postsService.addPost(jsonObject))
    }

    override suspend fun updatePostLocal(
        item: PostsListResponseItem
    ) {
        db.getPostsDao()
            .updatePost(
                item
            )
    }

    override suspend fun markPostAsDeleted(item: PostsListResponseItem) {
        db.getPostsDao()
            .updatePost(
                PostsListResponseItem(
                    item.id,
                    item.title,
                    item.body,
                    item.userId,
                    synced = false,
                    isDeleted = true
                )
            )
    }

    override suspend fun updatePostLocalAsSynced(
        item: PostsListResponseItem
    ) {
       val x= db.getPostsDao().updatePost(
            PostsListResponseItem(item.id, item.title, item.body, item.userId, synced = true)
        )
    }

    override suspend fun updatePostRemote(
        item: PostsListResponseItem
    ): Flow<PostsListResponseItem> {
        val jsonObject = JsonObject()
        jsonObject.addProperty("id", item.id)
        jsonObject.addProperty("title", item.title)
        jsonObject.addProperty("body", item.body)
        jsonObject.addProperty("userId", item.userId)
        return flowOf(postsService.updatePost(item.id!!, jsonObject))
    }

    override suspend fun deletePostLocal(item: PostsListResponseItem) {
        db.getPostsDao().deletePost(item)
    }

    override suspend fun deletePostRemote(
        id: Long
    ): Flow<PostsListResponseItem> {
        return flowOf(postsService.deletePost(id))
    }


}