package com.app.crudapp.core.networkExeption

import com.google.gson.Gson
import com.app.crudapp.core.networkExeption.ExceptionManger.NetworkStatus.GENERAL
import com.app.crudapp.core.networkExeption.ExceptionManger.NetworkStatus.INVALID_API_KEY
import okhttp3.ResponseBody
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

object ExceptionManger {
    private const val DEFAULT_ERROR_RESPONSE = "Network common error"
    private const val CONNECTION_ERROR_MSG = "Please Check Network Connection !"
    fun handleError(e: Throwable): NetworkException {
        return when (e) {
            is HttpException -> {
                when (getApiErrorType(e.response()?.errorBody())) {
                    INVALID_API_KEY -> {
                        Timber.tag("ExceptionManger").e("INVALID_API_KEY")
                        NetworkException.InvalidKey
                    }
                    GENERAL -> {
                        val apiErrorResponse = parseResponseError(e.response()?.errorBody())
                        Timber.tag("apiErrorResponse").e(apiErrorResponse)
                        NetworkException.NetworkError(e.code(), apiErrorResponse)
                    }
                }
            }

            is IOException -> {
                val errorMsg = e.message
                var userErrorMsg: String? = null
                if (errorMsg?.contains("jsonplaceholder.typicode") == true) {
                    userErrorMsg = CONNECTION_ERROR_MSG
                }
                Timber.tag("ExceptionManger").e(errorMsg)
                NetworkException.ConnectionError(userErrorMsg ?: errorMsg)
            }

            else -> {
                NetworkException.GeneralException(e.message)
            }
        }
    }

    private fun parseResponseError(errorBody: ResponseBody?): String {
        return try {
            val jsonData = Gson().fromJson(errorBody?.string(), CodeTextDataModel::class.java)
            val response = jsonData.codeText
            response
        } catch (e: Exception) {
            Timber.tag("parseResponseError").e(e)
            DEFAULT_ERROR_RESPONSE
        }
    }

    enum class NetworkStatus { INVALID_API_KEY, GENERAL }

    private fun getApiErrorType(errorBody: ResponseBody?): NetworkStatus {
      return try {
          val jsonData = Gson().fromJson(errorBody?.string(), CodeTextDataModel::class.java)
          val codeText = jsonData.codeText
          return when (codeText.lowercase()) {
              "invalid_api_key" -> INVALID_API_KEY
              else -> GENERAL
          }
      }catch (e:Exception){
          GENERAL
      }

    }
}
