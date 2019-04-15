package com.trebit.reststudy.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import com.trebit.reststudy.utils.SharedPref

/**
 * Jstargram
 * Class: BaseFragment
 * Created by kangjonghyuk on 15/04/2019.
 *
 * Description:
 */

open class BaseFragment: Fragment() {

    lateinit var mPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPref = SharedPref(activity!!)
    }
}