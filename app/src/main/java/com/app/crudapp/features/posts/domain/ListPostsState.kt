package com.app.crudapp.features.posts.domain

import androidx.annotation.Keep
import com.app.crudapp.core.networkExeption.NetworkException
import com.app.crudapp.features.posts.data.models.PostsListResponse
import com.app.crudapp.features.posts.data.models.PostsListResponseItem

@Keep
data class ListPostsState(
    val success: List<PostsListResponseItem>? = null,
    val failed: NetworkException? = null,
    val isLoading: Boolean = false
)
