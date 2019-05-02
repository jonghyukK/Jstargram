package com.trebit.reststudy.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trebit.reststudy.data.model.ContentItem
import com.trebit.reststudy.databinding.LayoutItemContentsVerticalBinding
import com.trebit.reststudy.utils.TimeUtils


/**
 * Jstargram
 * Class: ContentsAdapter
 * Created by kangjonghyuk on 25/04/2019.
 *
 * Description:
 */

class ContentsAdapter : RecyclerView.Adapter<ContentsAdapter.ViewHolder>() {

    private var mContentItems: MutableList<ContentItem> = ArrayList()
    lateinit var mContentEventListener: ContentEventListener

    fun setContentItem(lists: List<ContentItem>){
        mContentItems.clear()
        mContentItems.addAll(lists)
        notifyDataSetChanged()
    }

    fun updateContentItem(item: ContentItem, pos: Int) {
        mContentItems.remove(item)
        notifyItemRemoved(pos)
    }

    fun getContentItems(): List<ContentItem> = mContentItems

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val inflater = LayoutInflater.from(p0.context)
        val binding = LayoutItemContentsVerticalBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bind(mContentItems[p1], p1)
    }

    override fun getItemCount(): Int = mContentItems.size

    inner class ViewHolder(val binding: LayoutItemContentsVerticalBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ContentItem, position: Int){
            binding.contentItem = item

            // Contents Upload Time Circulation.
            binding.tvCreatedAt.text = TimeUtils.calculateContentDate(item.createdAt)

            binding.ivMore.setOnClickListener {
                mContentEventListener.clickedMore(item, position)
            }
        }
    }

    interface ContentEventListener {
        fun clickedMore(item: ContentItem, pos: Int)
    }
}