package com.app.crudapp.core.di.remote

import com.app.crudapp.features.posts.data.PostsRepositoryImpl
import com.app.crudapp.features.posts.domain.PostsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindPostsRepository(implementer: PostsRepositoryImpl): PostsRepository


}