package com.app.crudapp.core

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import com.app.crudapp.features.posts.presentation.WorkerHelper.setupWorker
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectivityListener @Inject constructor(
    private val context: Context
) : ConnectivityManager.NetworkCallback() {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun register() {
        connectivityManager.registerDefaultNetworkCallback(this)
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(this)
    }

    override fun onAvailable(network: Network) {
        Timber.tag(TAG).d("onAvailableConnection")
        setupWorker(context)
    }

    override fun onLost(network: Network) {
        Timber.tag(TAG).d("onLostConnection")
    }

    companion object {
        private const val TAG = "NetworkConnectivityList"
    }

}