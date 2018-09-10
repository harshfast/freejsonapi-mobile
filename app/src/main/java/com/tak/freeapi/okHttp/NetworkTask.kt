package com.tak.freeapi.okHttp

import com.tak.freeapi.BuildConfig
import com.tak.freeapi.common.*
import com.tak.freeapi.repository.Result
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.MalformedURLException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * [NetworkTask] executes the network request using okHttp Client.
 */
object NetworkTask {

    private const val TAG = "NetworkTask"

    private const val BASE_URL = BuildConfig.BASE_URL

    fun execute(urlPath: String): Result<DefaultResponse> {

        try {
            val request = Request.Builder()

                    .url(BASE_URL + urlPath).build()

            debugLog(TAG, "Host Url", "-----------------")
            debugLog(TAG, "${request.url()}")

            val response = OkHttpClient().newCall(request).execute()

            debugLog(TAG, "isSuccess: ${response.isSuccessful}")
            debugLog(TAG, "Response Code", "-----------------")
            debugLog(TAG, "${response.code()}")
            if (response.isSuccessful) {
                val responseBody = response?.body()?.string()
                debugLog(TAG, "Response", "-----------------")
                debugLog(TAG, "$responseBody")
                return Result.Success(DefaultResponse(responseBody))
            }
        } catch (exception: Exception) {
            debugLog(TAG, "Exception", "-----------------")
            logException(exception)
            val serviceException = when (exception) {
                is MalformedURLException -> ServiceException("$ERROR_INVALID_URL $exception.message")
                is SocketTimeoutException -> ServiceException("$ERROR_TIMEOUT $exception.message")
                is UnknownHostException -> ServiceException("$ERROR_INTERNET_NOT_AVAILABLE")
                else -> ServiceException(ERROR_CONNECTION_ERROR)
            }
            return Result.Error(serviceException)
        }
        return Result.Error(ServiceException(ERROR_CONNECTION_ERROR))
    }

}