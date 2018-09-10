package com.tak.freeapi.usecase.categories

import com.google.gson.Gson
import com.tak.freeapi.cache.room.ApiCategory
import com.tak.freeapi.cache.room.FreeApiCacheDatabase
import com.tak.freeapi.common.ERROR_INVALID_RESPONSE
import com.tak.freeapi.common.debugLog
import com.tak.freeapi.common.logException
import com.tak.freeapi.okHttp.DefaultResponse
import com.tak.freeapi.okHttp.NetworkTask
import com.tak.freeapi.okHttp.ServiceException
import com.tak.freeapi.repository.NetworkBoundResource
import com.tak.freeapi.repository.PATH_CATEGORIES
import com.tak.freeapi.repository.Result

/**
 * This class implements the logic to fetch categories list either from local db or from remote web service.
 */
class LoadApiCategories : NetworkBoundResource<List<ApiCategory>>() {

    private val TAG = LoadApiCategories::class.java.name
    private val freeApiCacheDb = FreeApiCacheDatabase.getInstance()

    /**
     * Stores categories list to local database.
     *
     * @param apiCategoryList - list of [ApiCategory]
     */
    override fun saveToLocal(apiCategoryList: List<ApiCategory>) {
        freeApiCacheDb.getCategoriesDao().insertAllCategories(apiCategoryList)
    }

    /**
     * Fetches categories list from local database
     *
     * @return result [Result]- list of [ApiCategory]
     */
    override fun fetchFromLocal(): Result<List<ApiCategory>> {
        return Result.Success(freeApiCacheDb.getCategoriesDao().getAllCategories())
    }

    override fun fetchFromRemote(): Result<List<ApiCategory>> {

        val response = NetworkTask.execute(PATH_CATEGORIES)
        return when (response) {
            is Result.Error -> response
            is Result.Success<DefaultResponse> -> {
                try {
                    debugLog(TAG, "fetchFromRemote ${response.data.responseBody}")

                    //TODO("Find a way to parse json array directly to object type")
                    val categoriesArray = Gson().fromJson(response.data.responseBody, Array<String>::class.java)
                    val categoryList = mutableListOf<ApiCategory>()
                    categoriesArray.forEach {
                        categoryList.add(ApiCategory(categoryName = it))
                    }
                    saveToLocal(categoryList)
                    Result.Success(categoryList)
                } catch (ex: Exception) {
                    logException(ex)
                    Result.Error(ServiceException(ERROR_INVALID_RESPONSE))
                }
            }
            else -> Result.Loading
        }
    }


    override fun shouldFetchFromRemote(): Boolean {
        return freeApiCacheDb.getCategoriesDao().getAllCategories().isEmpty()
    }


}