package com.app.crudapp.core.networkExeption

import androidx.annotation.Keep

sealed class NetworkException: Exception() {
    @Keep
    data class NetworkError(val code:Int,val error:String?): NetworkException()
    @Keep
    data class ConnectionError(val error:String?): NetworkException()
    @Keep
    data class GeneralException(val error:String?): NetworkException()

    object InvalidKey: NetworkException()

}
