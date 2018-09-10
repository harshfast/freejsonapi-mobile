package com.tak.freeapi.repository

import com.google.gson.Gson
import com.tak.freeapi.BuildConfig
import com.tak.freeapi.common.ERROR_INVALID_RESPONSE
import com.tak.freeapi.okHttp.ServiceException


abstract class NetworkBoundResource<T> {

    abstract fun saveToLocal(data: T)

    abstract fun shouldFetchFromRemote(): Boolean

    abstract fun fetchFromLocal(): Result<T>

    abstract fun fetchFromRemote(): Result<T>

    internal fun getResult(): Result<T> {
        if (shouldFetchFromRemote()) {
            return fetchFromRemote()
        } else {
            return fetchFromLocal()
        }
        return Result.Error(ServiceException(ERROR_INVALID_RESPONSE))
    }

    fun getUrl(path: String) = BuildConfig.BASE_URL + path

    @Throws(Exception::class)
    inline fun <reified T : Any> mapToResponseObject(responseJson: String?): T {
        return Gson().fromJson(responseJson, T::class.java)
    }

}