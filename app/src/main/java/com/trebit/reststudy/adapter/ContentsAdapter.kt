package com.trebit.reststudy.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.trebit.reststudy.data.model.ContentItem
import com.trebit.reststudy.data.model.ContentItems
import com.trebit.reststudy.databinding.LayoutItemContentsVerticalBinding

/**
 * Jstargram
 * Class: ContentsAdapter
 * Created by kangjonghyuk on 25/04/2019.
 *
 * Description:
 */

class ContentsAdapter : RecyclerView.Adapter<ContentsAdapter.ViewHolder>() {

    private var mContentItems: MutableList<ContentItem> = ArrayList()

    fun setContentItem(lists: List<ContentItem>){
        mContentItems.clear()
        mContentItems.addAll(lists)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val binding = LayoutItemContentsVerticalBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(mContentItems[p1])
    }

    override fun getItemCount(): Int = mContentItems.size

    inner class ViewHolder(val binding: LayoutItemContentsVerticalBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ContentItem){
            binding.contentItem = item
        }
    }
}