package com.tak.freeapi.repository

import com.tak.freeapi.cache.room.ApiCategory
import com.tak.freeapi.model.ApiResponse
import com.tak.freeapi.usecase.apis.LoadApis
import com.tak.freeapi.usecase.categories.LoadApiCategories


/**
 * Singleton object to connect with free api data sources for the application.
 */
object FreeApiRepository {

    /**
     * Loads categories of free api and returns the result [Result]
     *
     * return result Success or Failure [Result]
     */
    fun loadApiCategories(): Result<List<ApiCategory>> {
        return LoadApiCategories().getResult()
    }

    /**
     * Loads api list for the provided category and returns the result [Result]
     *
     * @param category
     * return result Success or Failure [Result]
     */
    fun loadApiByCategory(category: String): Result<ApiResponse> {
        return LoadApis(category).getResult()
    }

}