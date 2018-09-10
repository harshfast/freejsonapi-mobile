package com.tak.freeapi.viewmodel


import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.tak.freeapi.cache.room.ApiCategory
import com.tak.freeapi.model.Api
import com.tak.freeapi.model.ApiResponse
import com.tak.freeapi.repository.FreeApiRepository
import com.tak.freeapi.repository.Result
import com.tak.freeapi.worker.Worker

/**
 * [FreeApiListViewModel] implements the ui logic for both [FreeApiListFragment] and [FreeApiDetailsFragment]
 */
class FreeApiListViewModel : ViewModel() {

    var categoriesListLiveData: MutableLiveData<Result<List<ApiCategory>>> = MutableLiveData()
    var apiListLiveData: MutableLiveData<Result<ApiResponse>> = MutableLiveData()

    var loadedCategoriesList: List<ApiCategory>? = null
    var lastSelectedCategory: Int = 0

    companion object {
        const val DETAILS_DELIMITER = "##"
        const val CATEGORY_SPINNER_INDEX_ITEM = "Select Category"

        // Detials Label
        const val LABEL_CATEGORY = "Category"
        const val LABEL_NAME = "Name"
        const val LABEL_DESC = "Description"
        const val LABEL_LINK = "Link"
        const val LABEL_HTTPS = "Https Required"
        const val LABEL_AUTH = "Auth Required"
        const val LABEL_CORS = "CORS"

    }


    fun loadFreeApiCategories() {
        if (loadedCategoriesList == null) {
            categoriesListLiveData.postValue(Result.Loading)
            Worker.execute {
                categoriesListLiveData.postValue(FreeApiRepository.loadApiCategories())
            }
        }
    }

    fun loadApiByCategory(category: String) {
        apiListLiveData.postValue(Result.Loading)
        Worker.execute {
            apiListLiveData.postValue(FreeApiRepository.loadApiByCategory(category))
        }
    }

    /**
     * Prepares the list of api details items with prefix label separated by [DETAILS_DELIMITER] delimiter.
     *
     * @param apiData - [Api]
     */

    fun prepareApiDetailsList(apiData: Api): List<String> {
        val mutableList = mutableListOf<String>()

        mutableList.add("$LABEL_CATEGORY$DETAILS_DELIMITER${apiData.Category}")
        mutableList.add("$LABEL_NAME$DETAILS_DELIMITER${apiData.API}")
        mutableList.add("$LABEL_DESC$DETAILS_DELIMITER${apiData.Description}")
        mutableList.add("$LABEL_LINK$DETAILS_DELIMITER${apiData.Link}")
        mutableList.add("$LABEL_HTTPS$DETAILS_DELIMITER${apiData.HTTPS}")
        mutableList.add("$LABEL_AUTH$DETAILS_DELIMITER${apiData.Auth}")
        mutableList.add("$LABEL_CORS$DETAILS_DELIMITER${apiData.Cors}")

        return mutableList;
    }

    /**
     * Return the list for category spinner with index [CATEGORY_SPINNER_INDEX_ITEM] item added to show empty spinner view till
     * categories data loaded.
     */
    fun getEmptySpinnerList(): List<String> {
        val arrayList = mutableListOf<String>()
        arrayList.add(CATEGORY_SPINNER_INDEX_ITEM)
        return arrayList
    }

    fun getErrorMessage(errorCode: String?) = "Error: $errorCode"

    /**
     * Returns the last selected item of the category spinner if index item [CATEGORY_SPINNER_INDEX_ITEM] is
     * selected then return first item selected
     */
    fun getSelectedCategory() = if (lastSelectedCategory == 0) 1 else lastSelectedCategory


    /**
     * Converts list of ApiCategory to list of category names.
     * TODO(This logic should be moved to repository to return only list of category names)
     */
    fun getCategoryNameList(categoriesList: List<ApiCategory>?): List<String> {
        val categoriesNameList = mutableListOf<String>()
        categoriesList?.forEach {
            categoriesNameList.add(it.categoryName ?: "")
        }

        return categoriesNameList

    }

}