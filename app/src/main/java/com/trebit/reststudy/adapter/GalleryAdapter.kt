package com.trebit.reststudy.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.trebit.reststudy.R
import com.trebit.reststudy.data.model.GalleryItems
import kotlinx.android.synthetic.main.layout_item_gallery_img.view.*

/**
 * Jstargram
 * Class: GalleryAdapter
 * Created by kangjonghyuk on 17/04/2019.
 *
 * Description:
 */

class GalleryAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private val items = mutableListOf<GalleryItems>()

    fun replaceAll(newItems: List<GalleryItems>?) {
        newItems?.let {
            items.clear()
            items.addAll(it)
        }
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder{
        return GalleryViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.layout_item_gallery_img, p0, false))
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val holder = p0 as GalleryViewHolder
        holder.bindView(items[p1])
    }
}

class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(items: GalleryItems) {

//        Glide.with(itemView.context)
//            .load(items.path)
//            .override(80, 80)
//            .error(R.drawable.ic_mtrl_chip_close_circle)
//            .into(itemView.iv_galleyImg)
    }
}