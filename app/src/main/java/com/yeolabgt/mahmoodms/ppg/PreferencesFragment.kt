package com.yeolabgt.mahmoodms.ppg

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceFragment
import android.preference.PreferenceManager

/**
 * Created by mmahmood31 on 11/2/2017.
 *
 */

class PreferencesFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.prefs)
    }

    companion object {
        //Key Association:
        private var SETTINGS_CH_SELECT = "switch_ch"
        private var SETTINGS_SAVE_TIMESTAMPS = "timestamps"
        private var SETTINGS_SAVE_CLASS = "save_class"
        private var SETTINGS_BIT_PRECISION = "bit_precision"
        private var SETTINGS_FILTER_DATA = "filterData"

        fun channelSelect(context: Context): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_CH_SELECT, true)
        }

        fun saveTimestamps(context: Context): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_SAVE_TIMESTAMPS, false)
        }

        fun saveClass(context: Context): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_SAVE_CLASS, true)
        }

        fun setBitPrecision(context: Context): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_BIT_PRECISION, true)
        }

        fun setFilterData(context: Context): Boolean {
            return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SETTINGS_FILTER_DATA, false)
        }
    }
}
