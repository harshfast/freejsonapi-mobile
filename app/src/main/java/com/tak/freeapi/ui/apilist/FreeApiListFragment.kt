package com.tak.freeapi.ui.apilist

import android.app.Activity
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.tak.freeapi.R
import com.tak.freeapi.cache.room.ApiCategory
import com.tak.freeapi.common.debugLog
import com.tak.freeapi.common.getViewModel
import com.tak.freeapi.common.logException
import com.tak.freeapi.model.Api
import com.tak.freeapi.model.ApiResponse
import com.tak.freeapi.repository.Result
import com.tak.freeapi.viewmodel.FreeApiListViewModel
import kotlinx.android.synthetic.main.fragment_free_api_list.*


class FreeApiListFragment : Fragment() {

    private lateinit var categoriesSpinnerAdapter: ArrayAdapter<String>
    private lateinit var freeApiListAdapter: FreeApiListAdapter
    private lateinit var apiListViewModel: FreeApiListViewModel

    private lateinit var categorySpinner: Spinner
    private lateinit var apiRecyclerView: RecyclerView
    private lateinit var errorTextView: TextView
    private lateinit var countTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiListViewModel = (activity as AppCompatActivity).getViewModel()
        observeCategoriesResponse()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_free_api_list, container, false)
        initializeUiComponents(view)
        return view;
    }

    override fun onStart() {
        super.onStart()
        apiListViewModel.loadFreeApiCategories()
    }

    private fun initializeUiComponents(view: View) {
        //ApiCategory Spinner
        categorySpinner = view.findViewById(R.id.api_category_spinner)
        categorySpinner.onItemSelectedListener = onItemSelectListener
        categoriesSpinnerAdapter = getCategoryAdapter()
        categoriesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = categoriesSpinnerAdapter

        apiRecyclerView = view.findViewById(R.id.api_list_recycler_view)
        apiRecyclerView.layoutManager = LinearLayoutManager(context)
        freeApiListAdapter = FreeApiListAdapter(context as Activity, mutableListOf())
        apiRecyclerView.adapter = freeApiListAdapter
        apiRecyclerView.visibility = View.GONE

        countTextView = view.findViewById(R.id.count_textview)
        countTextView.visibility = View.GONE

        errorTextView = view.findViewById(R.id.error_textview)
        errorTextView.visibility = View.GONE

    }

    private fun getCategoryAdapter(): ArrayAdapter<String> {
        return ArrayAdapter(activity, android.R.layout.simple_dropdown_item_1line, apiListViewModel.getEmptySpinnerList())
    }


    private fun showCategories(categoriesData: List<ApiCategory>?) {
        categoriesSpinnerAdapter.addAll(apiListViewModel.getCategoryNameList(categoriesData))
        categoriesSpinnerAdapter.notifyDataSetChanged()
        categorySpinner.setSelection(apiListViewModel.getSelectedCategory())

    }

    private fun showErrorView(errorCode: String?) {
        errorTextView.visibility = View.VISIBLE
        errorTextView.text = apiListViewModel.getErrorMessage(errorCode)
    }

    private fun showApiList(apiList: List<Api>) {
        freeApiListAdapter.apiList = apiList
        freeApiListAdapter.notifyDataSetChanged()
    }


    private fun observeCategoriesResponse() {
        val categoriesObserver = Observer<Result<List<ApiCategory>>> {
            when (it) {
                is Result.Success<List<ApiCategory>> -> handleCategoriesResponse(it.data)
                is Result.Error -> {
                    logException(it.exception)
                    handleFailure(it.exception?.errorCode)
                }
                is Result.Loading -> handleProgress()
            }
        }

        apiListViewModel.categoriesListLiveData.observe(this, categoriesObserver)
    }

    private fun observeApisResponse() {

        val apiListObserver = Observer<Result<ApiResponse>> {
            when (it) {
                is Result.Success<ApiResponse> -> handleApiResponse(it.data)
                is Result.Loading -> handleProgress()
                is Result.Error -> {
                    logException(it.exception)
                    handleFailure(it.exception?.errorCode)
                }
            }
        }

        apiListViewModel.apiListLiveData.observe(this, apiListObserver)
    }

    private fun handleCategoriesResponse(apiCategories: List<ApiCategory>) {
        debugLog(message = apiCategories)
        progressBar.visibility = View.GONE
        showCategories(apiCategories)
    }

    private fun handleApiResponse(apiResponse: ApiResponse) {
        apiRecyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        errorTextView.visibility = View.GONE
        countTextView.visibility = View.VISIBLE
        countTextView.text = "${resources.getString(R.string.total_count)} ${apiResponse.count}"
        showApiList(apiResponse.entries.asList())
    }

    private fun handleProgress() {
        progressBar.visibility = View.VISIBLE
        apiRecyclerView.visibility = View.GONE
        countTextView.visibility = View.GONE
    }

    private fun handleFailure(errorCode: String?) {
        progressBar.visibility = View.GONE
        apiRecyclerView.visibility = View.GONE
        countTextView.visibility = View.GONE
        showErrorView(errorCode)
    }

    private val onItemSelectListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(p0: AdapterView<*>?) {
            debugLog(message = "Selected Nothing")
        }

        override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
            if (view != null && adapterView?.selectedItem != FreeApiListViewModel.CATEGORY_SPINNER_INDEX_ITEM) {
                debugLog(message = "Selected Item ${adapterView?.selectedItem}")
                observeApisResponse()
                apiListViewModel.lastSelectedCategory = position
                apiListViewModel.loadApiByCategory(adapterView?.selectedItem.toString())
            }
        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment FreeApiListFragment.
         */
        @JvmStatic
        fun newInstance() = FreeApiListFragment()
    }
}

