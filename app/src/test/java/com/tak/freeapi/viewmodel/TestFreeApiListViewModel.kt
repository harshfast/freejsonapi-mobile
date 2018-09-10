package com.tak.freeapi.viewmodel

import com.google.gson.Gson
import com.tak.freeapi.MOCK_VALID_CATEGORIES_RESPONSE
import com.tak.freeapi.cache.room.ApiCategory
import com.tak.freeapi.common.ERROR_INTERNET_NOT_AVAILABLE
import com.tak.freeapi.model.Api
import com.tak.freeapi.okHttp.DefaultResponse
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TestFreeApiListViewModel {

    private var apiListViewModel: FreeApiListViewModel? = null

    @Before
    fun setup() {
        apiListViewModel = FreeApiListViewModel()
    }

    @After
    fun teardown() {
        apiListViewModel = null
    }


    @Test
    fun test_prepareApiDetailsListWithSequence() {
        val apiData = Api("ApiName", "ApiDesc", "ApiAuth"
                , true, "cors", "link", "category")
        val list = apiListViewModel?.prepareApiDetailsList(apiData)
        Assert.assertNotNull(list)

        Assert.assertEquals(7, list?.size)

        Assert.assertEquals(FreeApiListViewModel.LABEL_CATEGORY, list?.get(0)?.substringBefore(FreeApiListViewModel.DETAILS_DELIMITER))
        Assert.assertEquals(apiData.Category, list?.get(0)?.substringAfter(FreeApiListViewModel.DETAILS_DELIMITER))


        Assert.assertEquals(FreeApiListViewModel.LABEL_NAME, list?.get(1)?.substringBefore(FreeApiListViewModel.DETAILS_DELIMITER))
        Assert.assertEquals(apiData.API, list?.get(1)?.substringAfter(FreeApiListViewModel.DETAILS_DELIMITER))


        Assert.assertEquals(FreeApiListViewModel.LABEL_DESC, list?.get(2)?.substringBefore(FreeApiListViewModel.DETAILS_DELIMITER))
        Assert.assertEquals(apiData.Description, list?.get(2)?.substringAfter(FreeApiListViewModel.DETAILS_DELIMITER))


        Assert.assertEquals(FreeApiListViewModel.LABEL_LINK, list?.get(3)?.substringBefore(FreeApiListViewModel.DETAILS_DELIMITER))
        Assert.assertEquals(apiData.Link, list?.get(3)?.substringAfter(FreeApiListViewModel.DETAILS_DELIMITER))


        Assert.assertEquals(FreeApiListViewModel.LABEL_HTTPS, list?.get(4)?.substringBefore(FreeApiListViewModel.DETAILS_DELIMITER))
        Assert.assertEquals(apiData.HTTPS.toString(), list?.get(4)?.substringAfter(FreeApiListViewModel.DETAILS_DELIMITER))


        Assert.assertEquals(FreeApiListViewModel.LABEL_AUTH, list?.get(5)?.substringBefore(FreeApiListViewModel.DETAILS_DELIMITER))
        Assert.assertEquals(apiData.Auth, list?.get(5)?.substringAfter(FreeApiListViewModel.DETAILS_DELIMITER))


        Assert.assertEquals(FreeApiListViewModel.LABEL_CORS, list?.get(6)?.substringBefore(FreeApiListViewModel.DETAILS_DELIMITER))
        Assert.assertEquals(apiData.Cors, list?.get(6)?.substringAfter(FreeApiListViewModel.DETAILS_DELIMITER))


    }

    @Test
    fun test_emptySpinnerList() {
        Assert.assertNotNull(apiListViewModel?.getEmptySpinnerList())
        Assert.assertEquals(FreeApiListViewModel.CATEGORY_SPINNER_INDEX_ITEM, apiListViewModel?.getEmptySpinnerList()?.get(0))
    }

    @Test
    fun test_errorMessage() {
        Assert.assertEquals("Error: $ERROR_INTERNET_NOT_AVAILABLE", apiListViewModel?.getErrorMessage(ERROR_INTERNET_NOT_AVAILABLE))
    }

    @Test
    fun test_getLastSelectedCategory() {
        apiListViewModel?.lastSelectedCategory = 5
        Assert.assertEquals(5, apiListViewModel?.getSelectedCategory())
    }

    @Test
    fun test_getLastSelectedCategoryForZeroShouldReturnFirstIndex() {
        apiListViewModel?.lastSelectedCategory = 0
        Assert.assertEquals(1, apiListViewModel?.getSelectedCategory())
    }


    @Test
    fun test_getCategoryNameList() {
        val categoriesArray = Gson().fromJson(DefaultResponse(MOCK_VALID_CATEGORIES_RESPONSE).responseBody, Array<String>::class.java)
        val categoryList = mutableListOf<ApiCategory>()
        categoriesArray.forEach {
            categoryList.add(ApiCategory(categoryName = it))
        }
        val namesList = apiListViewModel?.getCategoryNameList(categoryList)

        Assert.assertTrue(namesList?.isNotEmpty() ?: false)

    }

    @Test
    fun test_getCategoryNameListForNullShouldReturnEmptyList() {
        val namesList = apiListViewModel?.getCategoryNameList(null)
        Assert.assertTrue(namesList?.isEmpty() ?: false)
    }


}