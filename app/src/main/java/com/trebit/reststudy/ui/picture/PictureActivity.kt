package com.trebit.reststudy.ui.picture

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.orhanobut.logger.Logger
import com.trebit.reststudy.*
import com.trebit.reststudy.adapter.GalleryAdapter
import com.trebit.reststudy.databinding.ActivityPictureBinding
import com.trebit.reststudy.ui.BaseActivity
import com.trebit.reststudy.ui.picture.viewModel.PictureViewModel
import com.trebit.reststudy.utils.DisplayUtils.Companion.dpToPx
import com.trebit.reststudy.utils.DisplayUtils.Companion.getDisplayWH
import com.trebit.reststudy.utils.FileUtilJava
import com.trebit.reststudy.utils.FileUtils
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCrop.EXTRA_OUTPUT_URI
import com.yalantis.ucrop.UCrop.REQUEST_CROP
import com.yalantis.ucrop.UCropActivity.DEFAULT_COMPRESS_FORMAT
import com.yalantis.ucrop.UCropActivity.DEFAULT_COMPRESS_QUALITY
import com.yalantis.ucrop.callback.BitmapCropCallback
import com.yalantis.ucrop.view.*
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_picture.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/**
 * Jstargram
 * Class: PictureActivity
 * Created by kangjonghyuk on 22/04/2019.
 *
 * Description:
 */

class PictureActivity: BaseActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mViewModel     : PictureViewModel
    private lateinit var mBinding       : ActivityPictureBinding
    private lateinit var mCroppedImgUri : Uri

    val mLocalImages by lazy { mViewModel.getLocalImagesPath(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        mBinding   = DataBindingUtil.setContentView(this, R.layout.activity_picture)
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(PictureViewModel::class.java)
        mBinding.viewModel = mViewModel
        mBinding.activity  = this

        initView()
        initRecyclerView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView(){
        val width  = dpToPx(this, getDisplayWH(this, 0).toInt())
        val height = dpToPx(this, getDisplayWH(this, 0).toInt())

        mBinding.ivCropedImage.layoutParams.width  = width
        mBinding.ivCropedImage.layoutParams.height = height

        mViewModel.mCroppedImgUri.value = null

        mViewModel.mUploadResult.observe(this, Observer {
            it?.let {
                // When Upload Success.
                if ( it == RES_SUCCESS ) {
                    setResult(RESULT_OK)
                    finish()
                } else {
                    toast { getString(R.string.desc_upload_failed) }
                }
            }
        })
    }

    private fun initRecyclerView() {
        rv_galleryList.setHasFixedSize(true)
        rv_galleryList.adapter = GalleryAdapter {

            val sourceUri = Uri.fromFile(File(it.path))
            val tempFile = FileUtils.createImageFile()
            val destinyUri = Uri.fromFile(tempFile)

            callCropActivity(sourceUri, destinyUri)
        }
    }

    // Start CropActivity For Image Crop.
    private fun callCropActivity(sourceUri: Uri, destinationUri: Uri){
        val options = UCrop.Options()
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent))
        options.setMaxBitmapSize(10000)
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        options.setCompressionQuality(90)

        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(5f, 5f)
            .withOptions(options)
            .start(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            // Crop Result.
            if ( requestCode == REQUEST_CROP) {
                mCroppedImgUri = data?.extras?.get(EXTRA_OUTPUT_URI) as Uri
                mViewModel.mCroppedImgUri.value = mCroppedImgUri

                tv_descImgSelect.visibility = View.GONE
                sv_container.smoothScrollTo(0,0)
            }
        }
    }

    fun toolBarEvent(v: View) {
        when(v.id) {
            // 다음
            R.id.tv_next -> {
                // 사진 선택 X
                if ( !::mCroppedImgUri.isInitialized) {
                    AlertDialog.Builder(this)
                        .setMessage(getString(R.string.desc_img_not_selected))
                        .setPositiveButton(R.string.confirm){ _, _ -> }
                        .show()
                    return
                }

                // 다음 일떄
                if (tv_next.text == getString(R.string.next))
                    writeContentsWithImg()
                else
                    uploadContents()
            }
            // 취소
            R.id.tv_uploadCancell -> finish()
        }
    }

    private fun writeContentsWithImg(){
        val width  = dpToPx(this, 80)
        val height = dpToPx(this, 80)
        val margin = dpToPx(this, 20)

        mBinding.ivCropedImage.layoutParams.width  = width
        mBinding.ivCropedImage.layoutParams.height = height

        val lp = FrameLayout.LayoutParams(width, height)
        lp.setMargins(margin, margin, margin, margin)
        mBinding.ivCropedImage.layoutParams = lp
        mBinding.ivCropedImage.requestLayout()

        rv_galleryList.visibility = View.GONE
        tv_next.text = getString(R.string.upload)
    }

    private fun uploadContents(){

        val file = File(mCroppedImgUri.path)
        val test = FileProvider.getUriForFile(this, FILE_AUTHORITY, file)
        val iStream = contentResolver.openInputStream(test)
        val imgBytes = FileUtilJava.getBytes(iStream)

        val requestFile = RequestBody.create(MediaType.parse(contentResolver.getType(test)), imgBytes)
        val fileBody = MultipartBody.Part.createFormData("image", file.name, requestFile)

        mViewModel.uploadContent(
            content = et_inputContent.text.toString(),
            writer  = mPref.getPrefEmail(PREF_EMAIL),
            file    = fileBody)
    }
}