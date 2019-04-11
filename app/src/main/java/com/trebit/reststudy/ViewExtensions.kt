package com.trebit.reststudy

import android.content.Context
import android.content.ContextWrapper
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

/**
 * Jstargram
 * Class: ViewExtensions
 * Created by kangjonghyuk on 05/04/2019.
 *
 * Description:
 */


fun View.getParentActivity(): AppCompatActivity? {
    var context = this.context

    while (context is ContextWrapper) {
        if (context is AppCompatActivity)
            return context

        context = context.baseContext
    }
    return null
}


inline fun Context.toast(message: () -> String) {
    Toast.makeText(this, message(), Toast.LENGTH_SHORT).show()
}



inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction()
        .func()
        .addToBackStack(null)
        .commit()
}

inline fun FragmentManager.inTransactionNotStack(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction()
        .func()
        .commit()
}


fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}


fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { replace(frameId, fragment) }
}

fun AppCompatActivity.replaceFragmentNotStack(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransactionNotStack { replace(frameId, fragment) }
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

fun EditText.addTextWatcher(img: ImageView,
                            btn: Button? = null) {
    this.addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged (s: Editable?){}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
        override fun onTextChanged    (s: CharSequence?, start: Int, before: Int, count: Int){
            img.visibility = if ( this@addTextWatcher.text.isNotEmpty()) View.VISIBLE else View.GONE

            btn?.let { btn.isEnabled = this@addTextWatcher.text.isNotEmpty() }
        }
    })

    img.setOnClickListener { this.setText("") }
}

fun addTextWatcherDouble(img1 : ImageView,
                         img2 : ImageView,
                         btn  : Button?   = null,
                         et1  : EditText? = null,
                         et2  : EditText? = null
){
    var isFirstETField  = false
    var isSecondETField = false

    et1?.addTextChangedListener(object: TextWatcher{
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let{
                if ( s.isNotEmpty()){
                    img1.visibility = View.VISIBLE
                    isFirstETField  = true
                } else {
                    img1.visibility = View.GONE
                    isFirstETField = false
                }
            }

            btn?.isEnabled = isFirstETField && isSecondETField
            img1.setOnClickListener { et1.setText("") }
        }
    })

    et2?.addTextChangedListener(object: TextWatcher{
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let{
                if ( s.isNotEmpty()){
                    img2.visibility = View.VISIBLE
                    isSecondETField  = true
                } else {
                    img2.visibility = View.GONE
                    isSecondETField = false
                }
            }

            btn?.isEnabled = isFirstETField && isSecondETField
            img2.setOnClickListener { et2.setText("") }
        }
    })





}
