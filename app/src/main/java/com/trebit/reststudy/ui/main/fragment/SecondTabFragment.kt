package com.trebit.reststudy.ui.main.fragment

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.database.Cursor
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import com.trebit.reststudy.R
import com.trebit.reststudy.adapter.GalleryAdapter
import com.trebit.reststudy.data.model.GalleryItems
import com.trebit.reststudy.databinding.MainFragmentSecondTabBinding
import com.trebit.reststudy.ui.BaseFragment
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

class SecondTabFragment: BaseFragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mBinding: MainFragmentSecondTabBinding

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater          : LayoutInflater,
                              container         : ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment_second_tab, container, false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Logger.d(getAllImagesPath())

        rv_galleryList.layoutManager = GridLayoutManager(activity, 3)
        rv_galleryList.adapter = GalleryAdapter().apply { replaceAll(getAllImagesPath())}
    }

    private fun getAllImagesPath(): List<GalleryItems> {
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor : Cursor
        val column_idx_data : Int
        val column_idx_folder_name: Int
        var absolutePathOfImage: String? = null
        var lists = mutableListOf<GalleryItems>()

        val projection= arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        cursor = activity?.contentResolver!!.query(uri, projection, null, null, null)
        column_idx_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        column_idx_folder_name = cursor.getColumnIndexOrThrow((MediaStore.Images.Media.BUCKET_DISPLAY_NAME))

        while( cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_idx_data)
            lists.add(GalleryItems(absolutePathOfImage))
        }


        var listsss = mutableListOf<GalleryItems>()

        for ( i in 0 .. 20) {
//            lists.add(GalleryItems(absolutePathOfImage))

            listsss.add(lists[i])
        }

        return listsss
    }

    companion object {
        @JvmStatic
        fun newInstance() = SecondTabFragment()
    }

}