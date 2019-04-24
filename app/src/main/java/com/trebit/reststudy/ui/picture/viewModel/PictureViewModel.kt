package com.trebit.reststudy.ui.picture.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.trebit.reststudy.data.model.GalleryItems
import com.trebit.reststudy.data.remote.ApiService
import com.trebit.reststudy.data.repository.DataRepository
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Jstargram
 * Class: PictureViewModel
 * Created by kangjonghyuk on 22/04/2019.
 *
 * Description:
 */

@Singleton
class PictureViewModel @Inject constructor(
    apiService: ApiService
): ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }
    private val repository by lazy { DataRepository(apiService) }

    val mCroppedImgUri : MutableLiveData<Uri> = MutableLiveData()


    // Device Local Image 가져오기.
    fun getLocalImagesPath(ctx: Context): List<GalleryItems> {
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection= arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)

        val cursor = ctx.contentResolver.query(uri, projection, null, null, null)
        val columnIdxData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)
        var absolutePathOfImg = ""
        val localImgLists = mutableListOf<GalleryItems>()
        val localImgTests = mutableListOf<GalleryItems>()

        while (cursor.moveToNext()) {
            absolutePathOfImg = cursor.getString(columnIdxData)
            localImgLists.add(GalleryItems(absolutePathOfImg))
        }

        for ( i in 30 downTo 0) {
            localImgTests.add(localImgLists[i])
        }

        return localImgTests
    }

}