package com.tak.freeapi.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tak.freeapi.R
import com.tak.freeapi.cache.room.FreeApiCacheDatabase
import com.tak.freeapi.common.addFragment
import com.tak.freeapi.common.replaceFragment
import com.tak.freeapi.ui.apidetails.FreeApiDetailFragment
import com.tak.freeapi.ui.apilist.FreeApiListFragment

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Database
        FreeApiCacheDatabase.initDatabase(applicationContext)

        // This check to avoid adding the extra fragment on activity recreation scenarios
        if (savedInstanceState == null) {
            addFragment(FreeApiListFragment.newInstance(), R.id.fragment_container)
        }
    }


    override fun onBackPressed() {

        // Handling the back press on FreeApiDetailFragment
        when (supportFragmentManager.findFragmentById(R.id.fragment_container)) {
            is FreeApiDetailFragment -> replaceFragment(FreeApiListFragment.newInstance(), R.id.fragment_container)
            else -> super.onBackPressed()
        }

    }

}
