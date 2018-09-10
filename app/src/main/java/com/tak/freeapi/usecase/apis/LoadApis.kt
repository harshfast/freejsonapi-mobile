package com.tak.freeapi.usecase.apis

import com.google.gson.Gson
import com.tak.freeapi.cache.room.FreeApiCacheDatabase
import com.tak.freeapi.common.ERROR_INVALID_RESPONSE
import com.tak.freeapi.common.debugLog
import com.tak.freeapi.common.logException
import com.tak.freeapi.model.ApiResponse
import com.tak.freeapi.okHttp.DefaultResponse
import com.tak.freeapi.okHttp.NetworkTask
import com.tak.freeapi.okHttp.ServiceException
import com.tak.freeapi.repository.NetworkBoundResource
import com.tak.freeapi.repository.PATH_API_BY_CATEGORY
import com.tak.freeapi.repository.Result

private const val CATEGORY_DELIMITER = "&"

class LoadApis(val category: String) : NetworkBoundResource<ApiResponse>() {

    private val TAG = LoadApis::class.java.name
    private val freeApiCacheDb = FreeApiCacheDatabase.getInstance()

    override fun fetchFromLocal(): Result<ApiResponse> {
        val response = freeApiCacheDb.getCategoriesDao().loadApisForCategory(category)
        val apiResponse = mapToResponseObject<ApiResponse>(response.apiResponse)
        debugLog(TAG, "fetchFromLocal ${apiResponse.entries.size}")
        return Result.Success(apiResponse)
    }

    override fun saveToLocal(data: ApiResponse) {
        val dbCategory = freeApiCacheDb.getCategoriesDao().loadApisForCategory(category)
        dbCategory.apiResponse = Gson().toJson(data)
        freeApiCacheDb.getCategoriesDao().updateApiResponse(dbCategory)
    }

    override fun shouldFetchFromRemote(): Boolean {
        return freeApiCacheDb.getCategoriesDao().loadApisForCategory(category).apiResponse.isNullOrEmpty()
    }

    override fun fetchFromRemote(): Result<ApiResponse> {
        //Fitness = Fitness & Sports
        val categoryFirstName = category.substringBefore(CATEGORY_DELIMITER).trim()
        val response = NetworkTask.execute(PATH_API_BY_CATEGORY + categoryFirstName)
        return when (response) {
            is Result.Error -> response
            is Result.Success<DefaultResponse> -> {
                try {
                    debugLog(TAG, "fetchFromRemote ${response.data.responseBody}")
                    val apiResponse = mapToResponseObject<ApiResponse>(response.data.responseBody)
                    saveToLocal(apiResponse)
                    Result.Success(apiResponse)
                } catch (ex: Exception) {
                    logException(ex)
                    Result.Error(ServiceException(ERROR_INVALID_RESPONSE))
                }
            }
            else -> Result.Loading
        }
    }


}