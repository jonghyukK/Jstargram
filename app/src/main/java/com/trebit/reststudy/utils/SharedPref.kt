package com.trebit.reststudy.utils

import android.content.Context
import android.preference.PreferenceManager
import com.trebit.reststudy.DEFAULT_B
import com.trebit.reststudy.DEFAULT_S

/**
 * Jstargram
 * Class: SharedPref
 * Created by kangjonghyuk on 09/04/2019.
 *
 * Description:
 */

class SharedPref(val ctx: Context) {

    // Put Value into SharedPreferences.
    fun <U> putData(key: String, value: U)
            = with(PreferenceManager.getDefaultSharedPreferences(ctx).edit()) {

        when (value) {
            is String  -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            is Int     -> putInt(key, value)
            else -> throw IllegalArgumentException("This type cant be saved into Preferences")
        }.apply()
    }


    // Get Value from SharedPreferences.
    fun <T> getData(key: String, default: T): T
            = with(PreferenceManager.getDefaultSharedPreferences(ctx)) {

        val res: Any = when (default) {
            is String  -> getString(key, default)
            is Boolean -> getBoolean(key, default)
            is Int     -> getInt(key, default)
            else -> throw IllegalArgumentException("Data is Null")
        }
        res as T
    }

    fun isAutoLogin(key: String): Boolean = getData(key, DEFAULT_B)

    fun getPrefEmail(key: String): String = getData(key, DEFAULT_S)
    fun getPrefPw   (key: String): String = getData(key, DEFAULT_S)
}


