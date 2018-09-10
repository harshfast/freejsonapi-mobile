package com.tak.freeapi.ui.apidetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tak.freeapi.R
import com.tak.freeapi.common.getViewModel
import com.tak.freeapi.model.Api
import com.tak.freeapi.viewmodel.FreeApiListViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val API_DATA = "api_data"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FreeApiDetailFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FreeApiDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
private const val KEY_DATA = "savedInstanceStateData"

class FreeApiDetailFragment : Fragment() {

    private lateinit var apiData: Api


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            apiData = savedInstanceState.get(KEY_DATA) as Api
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_DATA, apiData)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_free_api_detail, container, false)

        val apiDetailRecyclerView = view.findViewById(R.id.api_detail_recycler_view) as RecyclerView
        apiDetailRecyclerView.layoutManager = LinearLayoutManager(context)
        val apiDetailsList = (activity as AppCompatActivity).getViewModel<FreeApiListViewModel>().prepareApiDetailsList(apiData)
        apiDetailRecyclerView.adapter = FreeApiDetailsAdapter(apiDetailsList)

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(_apiData: Api) =
                FreeApiDetailFragment().apply {
                    apiData = _apiData
                }
    }


}
