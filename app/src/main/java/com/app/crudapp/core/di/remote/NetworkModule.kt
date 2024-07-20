package com.app.crudapp.core.di.remote

import com.app.crudapp.BuildConfig
import com.app.crudapp.core.extensions.create
import com.app.crudapp.features.posts.data.remote.PostsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    internal object Constants {
        const val BASE_URL = "https://jsonplaceholder.typicode.com/"
        const val TIMEOUT_IN_SECONDS: Long = 120
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val log = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            log.level = HttpLoggingInterceptor.Level.BODY
        } else {
            log.level = HttpLoggingInterceptor.Level.NONE
        }
        return log
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient().newBuilder()
            .addInterceptor(httpLoggingInterceptor.apply {
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            })
            .readTimeout(Constants.TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(Constants.TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(Constants.TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    @Named("Normal")
    fun provideNormalRetrofit(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().addConverterFactory(gsonConverterFactory)
            .baseUrl(Constants.BASE_URL).client(okHttpClient).build()
    }

    @Singleton
    @Provides
    fun providePostsService(@Named("Normal") retrofit: Retrofit): PostsService =
        retrofit.create()


}