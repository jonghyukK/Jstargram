package com.trebit.reststudy.ui.profile

import android.Manifest
import android.app.Activity
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
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.trebit.reststudy.ui.customview.SelectEditActionDialog
import com.trebit.reststudy.utils.FileUtilJava
import com.trebit.reststudy.utils.FileUtils.Companion.createImageFile
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.IOException
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

    private var mTempFile: File? = null

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

    fun onClickEvent(v: View) {
        when(v.id) {
            // 취소
            R.id.tv_cancel -> finish()
            // 완료
            R.id.tv_doFinish -> updateUser()
            // 프로필 사진 바꾸기
            R.id.tv_editProfileImg -> selectAction()
        }
    }

    // 사진 촬영 or 갤러리 팝업.
    private fun selectAction() {
        SelectEditActionDialog(this, { takePhoto() }) { goToGallery() }.show()
    }

    // 사진 촬영.
    private fun takePhoto(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        try {
            mTempFile = createImageFile()
        } catch ( e: IOException) {
            e.printStackTrace()
        }

        if ( mTempFile != null) {
            mPhotoUri = FileProvider.getUriForFile(this, FILE_AUTHORITY, mTempFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri)
            startActivityForResult(intent, PICK_FROM_CAMERA)
        }
    }

    // 갤러리 선택.
    private fun goToGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = MediaStore.Images.Media.CONTENT_TYPE
        startActivityForResult(intent, PICK_FROM_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if ( resultCode != Activity.RESULT_OK){
            // Temp File Delete.
            mTempFile?.delete()
            return
        }

        when (requestCode) {
            PICK_FROM_ALBUM -> {
                if ( data == null) {
                    return
                }
                mPhotoUri = data.data
                cropImage()

                Logger.d("photoUri : $mPhotoUri")
            }
            PICK_FROM_CAMERA -> {
                cropImage()
                MediaScannerConnection.scanFile(this, arrayOf(mPhotoUri.path), null) { path, uri -> }
            }
            CROP_FROM_CAMERA -> {
                iv_editProfileImg.setImageURI(null)
                iv_editProfileImg.setImageURI(mPhotoUri)
                revokeUriPermission(mPhotoUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                Logger.d("croped img Path : $mPhotoUri")
            }
        }
    }

    private fun cropImage() {
        val intent = Intent(INTENT_CROP_ACTION)
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

            val folder = File(TEMP_FORDER_PATH)
            val tempFile = File(folder.toString(), croppedFileName?.name)
            mPhotoUri = FileProvider.getUriForFile(this, FILE_AUTHORITY, tempFile)

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


    // Update User Profile API.
    private fun updateUser() {
        if (et_editName.text.isEmpty()) {
            toast { getString(R.string.desc_input_name) }
            return
        }

        val name       : String = et_editName.text.toString()
        val introduce  : String = et_editIntroduce.text.toString()
        val email      : String = mPref.getPrefEmail(PREF_EMAIL)
        val profileImg : String?= mMainViewModel.myAccountInfo.value?.profile_img

        // Profile 변경 (x)
        if (!::mPhotoUri.isInitialized) {
            mViewModel.updateUser(
                name        = name,
                introduce   = introduce,
                email       = email,
                profile_img = profileImg)
        } else {
            // Profile 변경 (o)
            val file = File(mPhotoUri.path)
            val iStream = contentResolver.openInputStream(mPhotoUri)
            val imgBytes = FileUtilJava.getBytes(iStream)

            val requestFile = RequestBody.create(MediaType.parse(contentResolver.getType(mPhotoUri)), imgBytes)
            val fileBody = MultipartBody.Part.createFormData("image", file.name, requestFile)

            mViewModel.updateUserWithProfile(
                name      = name,
                introduce = introduce,
                email     = email,
                file      = fileBody
            )
        }
    }

    companion object {
        private const val PICK_FROM_CAMERA = 1
        private const val PICK_FROM_ALBUM  = 2
        private const val CROP_FROM_CAMERA = 3
    }
}