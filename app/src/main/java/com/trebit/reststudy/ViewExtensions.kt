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
import io.reactivex.internal.util.BackpressureHelper.add

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

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction { add(frameId, fragment) }
}

fun Fragment.addFragment(fragment: Fragment, frameId: Int) {
    fragmentManager?.inTransaction { add(frameId, fragment) }
}

fun AppCompatActivity.removeFragment() {
    if(supportFragmentManager.fragments.size > 0) {
        for ( i in 0 until supportFragmentManager.fragments.size) {
            val frag = supportFragmentManager.fragments[i]
            if (frag != null)
                supportFragmentManager.beginTransaction().remove(frag).commit()
        }
    }
}

fun Fragment.removeFragments() {
    fragmentManager?.let {
        if (it.fragments.size > 0) {
            for ( i in 0 until it.fragments.size) {
                it.beginTransaction().remove(it.fragments[i]).commit()
            }
        }
    }
}



fun EditText.addTextWatcher(img: ImageView) {
    addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                img.visibility = if ( s.isNotEmpty()) View.VISIBLE else View.GONE
            }

            img.setOnClickListener { setText("") }
        }
    })
}

fun addTextWatcherDouble(img1 : ImageView,
                         img2 : ImageView? = null,
                         btn  : Button?    = null,
                         et1  : EditText?  = null,
                         et2  : EditText?  = null
){
    var isFirstETField  = false
    var isSecondETField = false

    et1?.addTextChangedListener(object: TextWatcher{
        override fun afterTextChanged (s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged    (s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let{
                img1.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE
                isFirstETField  = s.isNotEmpty()
            }

            btn?.isEnabled = if ( et2 != null) isFirstETField && isSecondETField else isFirstETField
            img1.setOnClickListener { et1.setText("") }
        }
    })

    et2?.addTextChangedListener(object: TextWatcher{
        override fun afterTextChanged (s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged    (s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let{
                img2?.visibility = if (s.isNotEmpty()) View.VISIBLE else View.GONE
                isSecondETField  = s.isNotEmpty()
            }

            btn?.isEnabled = isFirstETField && isSecondETField
            img2?.setOnClickListener { et2.setText("") }
        }
    })





}
