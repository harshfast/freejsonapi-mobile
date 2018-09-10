package com.tak.freeapi.ui.apilist

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tak.freeapi.R
import com.tak.freeapi.common.replaceFragment
import com.tak.freeapi.model.Api
import com.tak.freeapi.ui.apidetails.FreeApiDetailFragment

class FreeApiListAdapter(val activityContext: Activity, var apiList: List<Api>) :
        RecyclerView.Adapter<FreeApiListAdapter.ApiListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ApiListViewHolder {
        val inflatedExpenseItemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_api_list, parent, false)
        return ApiListViewHolder(inflatedExpenseItemView)
    }

    override fun getItemCount(): Int {
        return apiList.size
    }

    override fun onBindViewHolder(holder: ApiListViewHolder?, position: Int) {

        holder?.apiNameTextView?.text = apiList[position].API
        holder?.apiDescTextView?.text = apiList[position].Description
        holder?.itemView?.setOnClickListener {
            (activityContext as AppCompatActivity).replaceFragment(FreeApiDetailFragment.newInstance(apiList[position]), R.id.fragment_container)
        }
    }


    class ApiListViewHolder(rowLayout: View) : RecyclerView.ViewHolder(rowLayout) {
        val apiNameTextView = rowLayout.findViewById<TextView>(R.id.api_name_textview)
        val apiDescTextView = rowLayout.findViewById<TextView>(R.id.api_desc_textview)
    }

}