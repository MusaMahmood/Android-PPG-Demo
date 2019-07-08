package com.yeolabgt.mahmoodms.upenndemo

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem

/**
 * Created by mmahmood31 on 11/2/2017.
 *
 */

class SettingsActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentManager!!.beginTransaction().replace(android.R.id.content, PreferencesFragment()).commit()
        val actionBar = actionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}
