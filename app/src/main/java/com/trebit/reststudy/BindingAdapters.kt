package com.trebit.reststudy

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.trebit.reststudy.adapter.GalleryAdapter
import com.trebit.reststudy.data.model.GalleryItems
import com.trebit.reststudy.utils.RecyclerItemDecoration
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Rest_study
 * Class: BindingAdapters
 * Created by kangjonghyuk on 02/04/2019.
 *
 * Description:
 */


// Profile Image Bind.
@BindingAdapter("bind_profile_img")
fun bindingProfileImage(
    view  : CircleImageView,
    value : String?
){
    if (value.isNullOrEmpty()) {
        view.setImageResource(R.drawable.icon_clear)
    } else {
        Glide.with(view.context)
            .load(BASE_API_URL + value)
            .fitCenter()
            .error(R.drawable.icon_clear)
            .into(view)
    }
}

// Local Image Bind.
@BindingAdapter("bind_local_img")
fun setImageUrl(view: ImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .optionalFitCenter()
        .error(R.drawable.icon_clear)
        .into(view)
}


// Content Image Bind.
@BindingAdapter("bind_content_img")
fun bindingConentImgData(view: ImageView, value: String?) {
    Glide.with(view.context)
        .load(BASE_API_URL + value)
        .centerCrop()
        .error(R.drawable.icon_clear)
        .into(view)
}


// Gallery Adapter Bind.
@BindingAdapter("bind_items")
fun bindingRecyclerItems(view: RecyclerView, items: List<GalleryItems>){
    view.addItemDecoration(RecyclerItemDecoration(view.context))
    val adapter = view.adapter as GalleryAdapter
    adapter.setGalleryItems(items)
    adapter.notifyDataSetChanged()
}


@BindingAdapter("cropImgSet")
fun bindingCropImageSet(view: ImageView, uri: MutableLiveData<Uri>?){
    val parentActivity = view.getParentActivity()

    if ( parentActivity != null && uri != null) {
        uri.observe(parentActivity, Observer { value ->
            view.setImageBitmap(BitmapFactory.decodeFile(value?.path))
        })
    }
}


