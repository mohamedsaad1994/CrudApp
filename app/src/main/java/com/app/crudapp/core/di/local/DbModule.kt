package com.app.crudapp.core.di.local

import android.content.Context
import com.app.crudapp.core.data.PostsDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbModule {
    @Singleton
    @Provides
    fun providePostsDao(@ApplicationContext context: Context): PostsDb {
        return PostsDb.invoke(context)
    }
}