package com.app.crudapp.features.posts.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

class PostsListResponse : ArrayList<PostsListResponseItem>()

@Entity
data class PostsListResponseItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long?=null,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "body")
    var body: String,
    @ColumnInfo(name = "userId")
    val userId: Int,
    @ColumnInfo(defaultValue = "false")
    val synced: Boolean?=false,
    @ColumnInfo(defaultValue = "false")
    val isDeleted: Boolean?=false
)