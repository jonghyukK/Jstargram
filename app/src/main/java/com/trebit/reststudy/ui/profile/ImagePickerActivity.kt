package com.trebit.reststudy.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import com.orhanobut.logger.Logger
import com.trebit.reststudy.ui.BaseActivity
import java.io.File
import java.lang.Exception

/**
 * Jstargram
 * Class: ImagePickerActivity
 * Created by kangjonghyuk on 15/04/2019.
 *
 * Description:   Not Used.
 */

//class ImagePickerActivity : BaseActivity() {
//
//    private val RESULT_SELECT_IMAGE = 100
//    val MEDIA_TYPE_IMAGE = 1
//
//    lateinit var mCurentPhotoPath: String
//    lateinit var mPhotoFile : File
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        try {
//            val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(intent, RESULT_SELECT_IMAGE)
//        } catch  (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        when (requestCode) {
//            RESULT_SELECT_IMAGE -> {
//                if ( resultCode == Activity.RESULT_OK && data != null && data.data != null){
//                    try {
//
//                        val selectedImage = data.data
//                        val filePathColumn = arrayOf( MediaStore.Images.Media.DATA )
//                        val cursor = contentResolver.query(selectedImage, filePathColumn, null, null, null)
//                        cursor.moveToFirst()
//                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
//                        val picturePath = cursor.getString(columnIndex)
//                        cursor.close()
//
//                        val intent = Intent()
//                        intent.putExtra("picturePath", picturePath)
//                        setResult(Activity.RESULT_OK, intent)
//                        finish()
//
//                    } catch ( e: Exception) {
//                        e.printStackTrace()
//                        val intent = Intent()
//                        setResult(Activity.RESULT_CANCELED, intent)
//                        finish()
//                    }
//                } else {
//                    Logger.e("RESULT_CANCELD")
//                    val intent = Intent()
//                    setResult(Activity.RESULT_CANCELED, intent)
//                    finish()
//                }
//            }
//        }
//    }
//}