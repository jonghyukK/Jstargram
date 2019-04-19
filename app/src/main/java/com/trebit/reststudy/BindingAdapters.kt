package com.trebit.reststudy

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.trebit.reststudy.adapter.GalleryAdapter
import com.trebit.reststudy.data.model.GalleryItems
import com.trebit.reststudy.data.model.UserVo
import com.trebit.reststudy.utils.GalleryItemDecoration
import de.hdodenhof.circleimageview.CircleImageView

/**
 * Rest_study
 * Class: BindingAdapters
 * Created by kangjonghyuk on 02/04/2019.
 *
 * Description:
 */


@BindingAdapter("textSet")
fun bindingTextData(
    view  : TextView,
    value : MutableLiveData<UserVo>?
) {
    val parentActivity = view.getParentActivity()

    if (parentActivity != null && value != null) {
        value.observe(parentActivity, Observer { value ->
            when (view.id) {
                R.id.tv_myEmailInfo -> view.text = value?.email
                R.id.tv_contentsCnt -> view.text = value?.contents_cnt.toString()
                R.id.tv_followerCnt -> view.text = value?.follower_cnt.toString()
                R.id.tv_followingCnt-> view.text = value?.following_cnt.toString()
                R.id.tv_userName    -> view.text = value?.name
                R.id.tv_introduce   -> view.text = value?.introduce
            }
        })
    }
}

@BindingAdapter("bindingImage")
fun bindingImageView(
    view  : CircleImageView,
    value : MutableLiveData<UserVo>?
){
    val parentActivity = view.getParentActivity()

    if (parentActivity != null && value != null){
        value.observe(parentActivity, Observer{ value ->
            Glide.with(view.context)
                .load(BASE_API_URL + value?.profile_img)
                .fitCenter()
                .error(R.drawable.icon_clear)
                .into(view)
        })
    }
}

@BindingAdapter("img")
fun setImageUrl(view: ImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .optionalFitCenter()
        .error(R.drawable.icon_clear)
        .into(view)
}

@BindingAdapter("bind_items")
fun bindingRecyclerItems(view: RecyclerView, items: List<GalleryItems>){
    view.addItemDecoration(GalleryItemDecoration(view.context))
    val adapter = view.adapter as GalleryAdapter
    adapter.setGalleryItems(items)
    adapter.notifyDataSetChanged()
}


