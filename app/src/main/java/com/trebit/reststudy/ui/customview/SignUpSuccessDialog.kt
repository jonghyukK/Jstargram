package com.trebit.reststudy.ui.customview

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.trebit.reststudy.R
import kotlinx.android.synthetic.main.dialog_sign_up_success.*

/**
 * Jstargram
 * Class: SignUpSuccessDialog
 * Created by kangjonghyuk on 12/04/2019.
 *
 * Description:
 */

class SignUpSuccessDialog(
    val ctx    : Context,
    val title  : String,
    val body   : String,
    val action : () -> Unit
): Dialog(ctx){

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_sign_up_success)
        setCanceledOnTouchOutside(false)

        tv_title.text = title
        tv_body .text = body
        btn_confirm.setOnClickListener { action() }
    }

    override fun onBackPressed() {}
}
