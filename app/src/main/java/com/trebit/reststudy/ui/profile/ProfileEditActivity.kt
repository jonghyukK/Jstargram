package com.trebit.reststudy.ui.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.view.View
import com.trebit.reststudy.*
import com.trebit.reststudy.databinding.ActivityProfileEditBinding
import com.trebit.reststudy.ui.BaseActivity
import com.trebit.reststudy.ui.main.viewmodel.MainViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_profile_edit.*
import java.io.File
import javax.inject.Inject
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.trebit.reststudy.ui.customview.SelectEditActionDialog
import kotlinx.android.synthetic.main.dialog_profile_edit_action.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * Jstargram
 * Class: ProfileEditActivity
 * Created by kangjonghyuk on 11/04/2019.
 *
 * Description:
 */

class ProfileEditActivity: BaseActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mViewModel     : ProfileViewModel
    private lateinit var mMainViewModel : MainViewModel
    private lateinit var mBinding       : ActivityProfileEditBinding
    private lateinit var mPhotoUri      : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        mBinding    = DataBindingUtil.setContentView(this, R.layout.activity_profile_edit)
        mViewModel  = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        mMainViewModel         = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        mBinding.viewModel     = mViewModel
        mBinding.mainViewModel = mMainViewModel
        mBinding.activity      = this

        init()
    }

    private fun init() {
        et_editName.addTextWatcher(iv_clearEditName)

        mViewModel.myProfileInfo.observe(this, Observer { value ->
            value?.let {
                val intent = Intent()
                intent.putExtra(INTENT_PROFILE_DATA, value)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })
    }

    private fun checkPermissions(): Boolean {
        var result: Int
        val permissionList = ArrayList<String>()
        for (pm in PERMISSIONS) {
            result = ContextCompat.checkSelfPermission(this, pm)
            if (result != PackageManager.PERMISSION_GRANTED)
                permissionList.add(pm)
        }

        if (permissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toTypedArray(), REQ_MULTIPLE_PERMISSIONS)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            REQ_MULTIPLE_PERMISSIONS -> {

                for ( i in 0 until permissions.size) {
                    when ( permissions[i]) {
                        PERMISSIONS[0] -> {
                            if ( grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish()
                                return
                                }
                        }
                        PERMISSIONS[1] -> {
                            if ( grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish()
                                return
                            }
                        }
                        PERMISSIONS[2] -> {
                            if ( grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish()
                                return
                            }
                        }
                    }
                }
                selectAction()
            }
        }
    }

    private fun showNoPermissionToastAndFinish() {
        toast { "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다."}
    }

    fun onClickEvent(v: View) {
        when(v.id) {
            // 취소
            R.id.tv_cancel         -> finish()
            // 완료
            R.id.tv_doFinish       -> updateUser()
            // 프로필 사진 바꾸기
            R.id.tv_editProfileImg -> if (checkPermissions()) { selectAction() }
        }
    }

    private fun selectAction() {
        SelectEditActionDialog(this, { takePhoto() }) { goToGallery() }.show()
    }

    // 사진 촬영.
    private fun takePhoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null

        try {
            photoFile = createImageFile()
        } catch ( e: IOException) {
            e.printStackTrace()
        }

        if ( photoFile != null) {
            mPhotoUri = FileProvider.getUriForFile(this, "com.trebit.reststudy.provider", photoFile)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
            startActivityForResult(intent, PICK_FROM_CAMERA)
        }
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("HHmmss").format(Date())
        val imageFileName = "kangtest_${timeStamp}_"
        val storageDir = File("${Environment.getExternalStorageDirectory()}/KangTest/")
        if ( !storageDir.exists()) {
            storageDir.mkdirs()
        }

        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    // 갤러리 선택.
    private fun goToGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ( resultCode != Activity.RESULT_OK){
            toast { "do Canceled1" }
            return
        }

        when (requestCode) {
            PICK_FROM_ALBUM -> {
                if ( data == null) {
                    return
                }
                mPhotoUri = data.data
                cropImage()
            }
            PICK_FROM_CAMERA -> {
                cropImage()
                MediaScannerConnection.scanFile(this, arrayOf(mPhotoUri.path), null) { path, uri -> }
            }
            CROP_FROM_CAMERA -> {
                iv_editProfileImg.setImageURI(null)
                iv_editProfileImg.setImageURI(mPhotoUri)
                revokeUriPermission(mPhotoUri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        }
    }

    private fun cropImage() {
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(mPhotoUri, "image/*")
        val list = packageManager.queryIntentActivities(intent, 0)
        grantUriPermission(list[0].activityInfo.packageName, mPhotoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val size = list.size
        if ( size == 0 ){
            toast { "do Canceled2" }
            return
        } else {
            intent.putExtra("crop", "true")
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            intent.putExtra("scale", true)

            var croppedFileName : File? = null

            try {
                croppedFileName = createImageFile()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            val folder = File("${Environment.getExternalStorageDirectory()}/KangTest/")
            val tempFile = File(folder.toString(), croppedFileName?.name)
            mPhotoUri = FileProvider.getUriForFile(this,
                "com.trebit.reststudy.provider", tempFile)
            intent.putExtra("return-data", false)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())

            val i = Intent(intent)
            val res = list[0]
            grantUriPermission(res.activityInfo.packageName, mPhotoUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            i.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            startActivityForResult(i, CROP_FROM_CAMERA)
        }
    }


    private fun updateUser() {
        if (et_editName.text.isEmpty()) {
            toast { getString(R.string.desc_input_name) }
            return
        }

        mViewModel.updateUser(
            email       = mPref.getPrefEmail(PREF_EMAIL),
            name        = et_editName     .text.toString(),
            introduce   = et_editIntroduce.text.toString(),
            profile_img = "profile_path")
    }

    companion object {
        private const val PICK_FROM_CAMERA = 1
        private const val PICK_FROM_ALBUM  = 2
        private const val CROP_FROM_CAMERA = 3

        private val PERMISSIONS = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA)
    }
}