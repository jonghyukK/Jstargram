package com.trebit.reststudy

import android.content.ContextWrapper
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.View

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
    val backStackEntry = supportFragmentManager.backStackEntryCount
    if ( backStackEntry > 0 ){
        for ( i in 0 until backStackEntry) {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    if(supportFragmentManager.fragments.size > 0) {
        for ( i in 0 until supportFragmentManager.fragments.size) {
            val frag = supportFragmentManager.fragments[i]
            if ( frag != null)
                supportFragmentManager.beginTransaction().remove(frag).commit()
        }
    }
}
