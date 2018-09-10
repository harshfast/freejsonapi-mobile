package com.tak.freeapi.ui.apidetails

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tak.freeapi.R
import com.tak.freeapi.viewmodel.FreeApiListViewModel

class FreeApiDetailsAdapter(val apiDetails: List<String>) :
        RecyclerView.Adapter<FreeApiDetailsAdapter.ApiDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ApiDetailsViewHolder {
        val inflatedExpenseItemView = LayoutInflater.from(parent?.context).inflate(R.layout.item_api_details, parent, false)
        return ApiDetailsViewHolder(inflatedExpenseItemView)
    }

    override fun getItemCount(): Int {
        return apiDetails.size
    }

    override fun onBindViewHolder(holder: ApiDetailsViewHolder?, position: Int) {

        holder?.labelTextView?.text = apiDetails[position].substringBefore(FreeApiListViewModel.DETAILS_DELIMITER)
        holder?.detailsTextView?.text = apiDetails[position].substringAfter(FreeApiListViewModel.DETAILS_DELIMITER)

    }


    class ApiDetailsViewHolder(rowLayout: View) : RecyclerView.ViewHolder(rowLayout) {
        val labelTextView = rowLayout.findViewById<TextView>(R.id.label_textView)
        val detailsTextView = rowLayout.findViewById<TextView>(R.id.details_textView)
    }
}