package com.app.crudapp.application

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.app.crudapp.BuildConfig
import com.app.crudapp.core.NetworkConnectivityListener
import com.app.crudapp.features.posts.presentation.WorkerHelper.setupWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class CrudApplication : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var networkConnectivityListener: NetworkConnectivityListener

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        networkConnectivityListener.register()

        applicationScope.launch {
            setupWorker(applicationContext)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        networkConnectivityListener.unregister()
    }

}