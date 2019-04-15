package com.trebit.reststudy.ui.customview

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.trebit.reststudy.R
import kotlinx.android.synthetic.main.dialog_profile_edit_action.*

/**
 * Jstargram
 * Class: SelectEditActionDialog
 * Created by kangjonghyuk on 15/04/2019.
 *
 * Description:
 */

class SelectEditActionDialog(
    val ctx          : Context,
    val actionCamera : () -> Unit,
    val actionGallery: () -> Unit
): Dialog(ctx) {

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_profile_edit_action)

        tv_takePhoto.setOnClickListener { actionCamera(); dismiss() }
        tv_gallery.setOnClickListener { actionGallery(); dismiss() }
    }
}