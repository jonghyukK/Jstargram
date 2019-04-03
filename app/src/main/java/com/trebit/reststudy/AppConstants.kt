package com.trebit.reststudy

import android.app.Fragment
import android.content.ContextWrapper
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View
import io.reactivex.internal.util.BackpressureHelper.add

/**
 * TrebitM-AOS
 * Class: AppConstants
 * Created by kangjonghyuk on 05/03/2019.
 *
 * Description:
 */


const val BASE_API_URL = "http://192.168.2.1:3000/"


fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context

    while (context is ContextWrapper) {
        if (context is AppCompatActivity)
            return context

        context = context.baseContext
    }
    return null
}


inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction()
        .func()
        .addToBackStack(null)
        .commit()
}

fun AppCompatActivity.addFragment(fragment: android.support.v4.app.Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun AppCompatActivity.replaceFragment(fragment: android.support.v4.app.Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

fun AppCompatActivity.removeFragment() {
    supportFragmentManager.inTransaction {
        remove(supportFragmentManager.findFragmentById(R.id.fl_container)!!)
    }
}
