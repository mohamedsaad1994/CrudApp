package com.app.crudapp.features.posts.data.remote

import com.app.crudapp.features.posts.data.models.PostsListResponse
import com.app.crudapp.features.posts.data.models.PostsListResponseItem
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostsService {
    @GET("posts")
    suspend fun getPosts(): PostsListResponse

    @POST("posts")
    suspend fun addPost(
        @Body jsonObject: JsonObject
    ): PostsListResponseItem

    @PUT("posts/{id}")
    suspend fun updatePost(
        @Path("id") id: Long,
        @Body jsonObject: JsonObject
    ): PostsListResponseItem

    @DELETE("posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Long,
    ): PostsListResponseItem
}