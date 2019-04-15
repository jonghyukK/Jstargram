package com.trebit.reststudy.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.trebit.reststudy.utils.SharedPref

/**
 * Jstargram
 * Class: BaseActivity
 * Created by kangjonghyuk on 09/04/2019.
 *
 * Description:
 */

open class BaseActivity: AppCompatActivity() {

    lateinit var mPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPref = SharedPref(this)
    }


}