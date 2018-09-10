package com.tak.freeapi

import com.google.gson.Gson
import com.tak.freeapi.cache.room.ApiCategory
import com.tak.freeapi.common.ERROR_INVALID_RESPONSE
import com.tak.freeapi.model.ApiResponse
import com.tak.freeapi.okHttp.DefaultResponse
import com.tak.freeapi.okHttp.ServiceException
import com.tak.freeapi.repository.NetworkBoundResource
import com.tak.freeapi.repository.Result
import org.junit.Assert
import org.junit.Test


class TestFreeApiRepository {


    @Test
    fun test_getResultForValidCategoriesResponse() {

        // Given
        var categoryList = mutableListOf<ApiCategory>()
        val loadCategories = object : NetworkBoundResource<List<ApiCategory>>() {
            override fun saveToLocal(data: List<ApiCategory>) {
                categoryList = data.toMutableList()
            }

            override fun fetchFromLocal(): Result<List<ApiCategory>> {
                return Result.Success(categoryList)
            }

            override fun shouldFetchFromRemote(): Boolean {
                return categoryList.isEmpty()
            }

            override fun fetchFromRemote(): Result<List<ApiCategory>> {
                val response = DefaultResponse(MOCK_VALID_CATEGORIES_RESPONSE)
                val categoriesArray = Gson().fromJson(response.responseBody, Array<String>::class.java)

                categoriesArray.forEach {
                    categoryList.add(ApiCategory(categoryName = it))
                }
                return Result.Success(categoryList)
            }

        }

        // When
        val response = loadCategories.getResult()

        // Then
        when (response) {
            is Result.Success<List<ApiCategory>> -> {
                Assert.assertNotNull(response.data)
                Assert.assertTrue(response.data.isNotEmpty())
            }
        }

    }

    @Test
    fun test_getResultForValidApiResponse() {
        val apiResponseBody = DefaultResponse(MOCK_CATEGORY_ANIMALS_RESPONSE)
        var apiResponse: ApiResponse
        val loadCategories = object : NetworkBoundResource<ApiResponse>() {

            override fun saveToLocal(data: ApiResponse) {
                apiResponse = data
            }

            override fun fetchFromLocal(): Result<ApiResponse> {
                apiResponse = mapToResponseObject(apiResponseBody.responseBody)
                return Result.Success(apiResponse)
            }


            override fun shouldFetchFromRemote(): Boolean {
                return true
            }

            override fun fetchFromRemote(): Result<ApiResponse> {
                apiResponse = mapToResponseObject(apiResponseBody.responseBody)
                return Result.Success(apiResponse)
            }

        }

        val response = loadCategories.getResult()
        when (response) {
            is Result.Success<ApiResponse> -> {

                Assert.assertNotNull(response.data)
                Assert.assertTrue(response.data.count > 0)
                Assert.assertTrue(response.data.entries.isNotEmpty())
                Assert.assertEquals("Animals", response.data.entries.get(0).Category)
            }
        }

    }


    @Test
    fun test_getResultForEmptyCategoryArray() {
        val apiResponseBody = DefaultResponse(MOCK_CATEGORY_EMPTY_RESPONSE)
        var apiResponse: ApiResponse
        val loadCategories = object : NetworkBoundResource<ApiResponse>() {

            override fun saveToLocal(data: ApiResponse) {
                apiResponse = data
            }

            override fun fetchFromLocal(): Result<ApiResponse> {
                apiResponse = mapToResponseObject(apiResponseBody.responseBody)
                return Result.Success(apiResponse)
            }


            override fun shouldFetchFromRemote(): Boolean {
                return true
            }

            override fun fetchFromRemote(): Result<ApiResponse> {
                try {
                    apiResponse = mapToResponseObject(apiResponseBody.responseBody)
                    return Result.Success(apiResponse)
                } catch (ex: Exception) {
                    return Result.Error(ServiceException(ERROR_INVALID_RESPONSE))
                }
            }

        }

        val response = loadCategories.getResult()
        when (response) {
            is Result.Error -> {

                Assert.assertNotNull(response)
                Assert.assertEquals(ServiceException(ERROR_INVALID_RESPONSE), response.exception)
            }
        }
    }


    @Test
    fun test_getResultForNotJsonResponse() {

        val apiResponseBody = DefaultResponse(MOCK_CATEGORY_NOT_JSON)
        var apiResponse: ApiResponse
        val loadCategories = object : NetworkBoundResource<ApiResponse>() {

            override fun saveToLocal(data: ApiResponse) {
                apiResponse = data
            }

            override fun fetchFromLocal(): Result<ApiResponse> {
                apiResponse = mapToResponseObject(apiResponseBody.responseBody)
                return Result.Success(apiResponse)
            }


            override fun shouldFetchFromRemote(): Boolean {
                return true
            }

            override fun fetchFromRemote(): Result<ApiResponse> {
                try {
                    apiResponse = mapToResponseObject(apiResponseBody.responseBody)
                    return Result.Success(apiResponse)
                } catch (ex: Exception) {
                    return Result.Error(ServiceException(ERROR_INVALID_RESPONSE))
                }
            }

        }

        Assert.assertTrue(loadCategories.shouldFetchFromRemote())

        val response = loadCategories.getResult()
        when (response) {
            is Result.Error -> {
                Assert.assertNotNull(response)
                Assert.assertEquals(ServiceException(ERROR_INVALID_RESPONSE), response.exception)
            }
        }

    }


}