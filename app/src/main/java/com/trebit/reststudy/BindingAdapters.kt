package com.trebit.reststudy

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

/**
 * Rest_study
 * Class: BindingAdapters
 * Created by kangjonghyuk on 02/04/2019.
 *
 * Description:
 */


@BindingAdapter("clearImage")
fun bindingClearImg(view: ImageView, text: MutableLiveData<String>?) {
    val parentActivity = view.getParentActivity()

    if (parentActivity != null && text != null) {
        text.observe(parentActivity, Observer {
            text.value?.let {
                view.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
            }
        })
    }
}

@BindingAdapter("textEmail", "textPw")
fun bindingBtnEnable(
    view  : Button,
    email : MutableLiveData<String>?,
    pw    : MutableLiveData<String>?
) {
    val parentActivity = view.getParentActivity()

    if (parentActivity != null && email != null && pw != null) {
        email.observe(parentActivity, Observer { value1 ->
            pw.observe(parentActivity, Observer { value2 ->
                view.isEnabled = value1!!.isNotEmpty() && value2!!.isNotEmpty()
            })
        })
    }
}


@BindingAdapter("textValue")
fun bindingBtnEnable2(
    view  : Button,
    value : MutableLiveData<String>?
) {
    val parentActivity = view.getParentActivity()

    if (parentActivity != null && value != null) {
        value.observe(parentActivity, Observer { value ->
            view.isEnabled = value!!.isNotEmpty()
        })
    }
}

