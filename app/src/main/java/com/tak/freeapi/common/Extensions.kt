package com.tak.freeapi.common

import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * This file defines kotlin extension functions
 */

/**
 * Extension function added to [AppCompatActivity] to add the given fragment.
 */
fun AppCompatActivity.addFragment(fragment: Fragment, containerId: Int) {
    supportFragmentManager.beginTransaction().add(containerId, fragment).commit()
}

/**
 * Extension function added to [AppCompatActivity] to replace the given fragment.
 */
fun AppCompatActivity.replaceFragment(fragment: Fragment, containerId: Int) {
    supportFragmentManager.beginTransaction().replace(containerId, fragment).commit()
}

/**
 * Extension function added to [AppCompatActivity] to get the view model [ViewModel].
 */
inline fun <reified viewModel : ViewModel> AppCompatActivity.getViewModel() =
        ViewModelProviders.of(this).get(viewModel::class.java)

/**
 * Extension function added to [Activity] to check the network connectivity.
 */

fun Activity.isNetworkConnected(): Boolean {
    val connectivityManager =
            this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}


