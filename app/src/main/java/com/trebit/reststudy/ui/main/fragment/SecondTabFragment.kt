package com.trebit.reststudy.ui.main.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.trebit.reststudy.R
import com.trebit.reststudy.adapter.GalleryAdapter
import com.trebit.reststudy.databinding.MainFragmentSecondTabBinding
import com.trebit.reststudy.ui.BaseFragment
import com.trebit.reststudy.ui.main.viewmodel.MainViewModel
import com.trebit.reststudy.utils.FileUtils
import com.yalantis.ucrop.view.GestureCropImageView
import com.yalantis.ucrop.view.OverlayView
import com.yalantis.ucrop.view.UCropView
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.main_fragment_second_tab.*
import javax.inject.Inject


/**
 * Jstargram
 * Class: SecondTabFragment
 * Created by mac on 05/04/2019.
 *
 * Description:
 */

class SecondTabFragment: BaseFragment(){

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mBinding       : MainFragmentSecondTabBinding
    private lateinit var mMainViewModel : MainViewModel

    private lateinit var mUCropView: UCropView
    private lateinit var mGestureCropImageView: GestureCropImageView
    private lateinit var mOverlayView: OverlayView

    val localImages by lazy { FileUtils.getLocalImagesPath(activity!!)}

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater          : LayoutInflater,
                              container         : ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        mBinding       = DataBindingUtil.inflate(inflater, R.layout.main_fragment_second_tab, container, false)
        mMainViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)
        mBinding.fragment = this

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateRootViews(view)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
    }


    private fun initiateRootViews(view: View) {
        mUCropView = view.findViewById(com.yalantis.ucrop.R.id.ucrop)
        mGestureCropImageView = mUCropView.cropImageView
        mOverlayView = mUCropView.overlayView

//        mGestureCropImageView.setTransformImageListener(mImageListener)

        (view.findViewById(com.yalantis.ucrop.R.id.image_view_logo) as ImageView).setColorFilter(
            activity!!.getColor(R.color.blue_malibu),
            PorterDuff.Mode.SRC_ATOP
        )

//        view.findViewById(com.yalantis.ucrop.R.id.ucrop_frame).setBackgroundColor(mRootViewBackgroundColor)
    }


    private fun initRecyclerView() {
        rv_galleryList.setHasFixedSize(true)
        rv_galleryList.adapter = GalleryAdapter {

//            Glide.with(this)
//                .load(it.path)
//                .optionalFitCenter()
//                .error(R.drawable.icon_clear)
//                .into(iv_selectedImg)

            Logger.d("image Click Path : ${it.path}")

            val pictureIntent = Intent(Intent.ACTION_GET_CONTENT)
            pictureIntent.type = "image/*"
            pictureIntent.addCategory(Intent.CATEGORY_OPENABLE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val mimeTypes = arrayOf("image/jpeg", "image/png")
                pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
            activity!!.startActivityForResult(
                Intent.createChooser(pictureIntent, "Select Picture"),
                100
            )

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SecondTabFragment()
    }

}