package com.trebit.reststudy.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.trebit.reststudy.data.model.GalleryItems
import com.trebit.reststudy.databinding.LayoutItemGalleryBinding

/**
 * Jstargram
 * Class: GalleryAdapter
 * Created by kangjonghyuk on 18/04/2019.
 *
 * Description:
 */

class GalleryAdapter(
    val onClick: (GalleryItems) -> Unit
): RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    private var mGalleryItems : MutableList<GalleryItems> = ArrayList()

    fun setGalleryItems(lists : List<GalleryItems>) {
        mGalleryItems.clear()
        mGalleryItems.addAll(lists)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val binding = LayoutItemGalleryBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(mGalleryItems[p1])
        p0.itemView.setOnClickListener { onClick(mGalleryItems[p1]) }
    }

    override fun getItemCount(): Int = mGalleryItems.size

    inner class ViewHolder(val binding: LayoutItemGalleryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: GalleryItems){
            binding.galleryItem = item
        }
    }
}