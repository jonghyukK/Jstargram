package com.trebit.reststudy.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import com.trebit.reststudy.VIEW_TYPE_GRID
import com.trebit.reststudy.VIEW_TYPE_VERTICAL
import com.trebit.reststudy.data.model.ContentItem
import com.trebit.reststudy.databinding.LayoutItemContentsGridBinding
import com.trebit.reststudy.databinding.LayoutItemContentsVerticalBinding
import com.trebit.reststudy.utils.TimeUtils


/**
 * Jstargram
 * Class: ContentsAdapter
 * Created by kangjonghyuk on 25/04/2019.
 *
 * Description:
 */

class ContentsAdapter(private val viewType: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContentItems : MutableList<ContentItem> = ArrayList()
    lateinit var mContentEventListener : ContentEventListener

    fun setContentItem(lists: List<ContentItem>){
        mContentItems.clear()
        mContentItems.addAll(lists)
        notifyDataSetChanged()
    }

    fun deleteContentItem(item: ContentItem, pos: Int) {
        mContentItems.remove(item)
        notifyItemRemoved(pos)
    }

    override fun getItemViewType(position: Int): Int {
        return if ( viewType == VIEW_TYPE_GRID ) VIEW_TYPE_GRID else VIEW_TYPE_VERTICAL
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when ( viewType ) {
            VIEW_TYPE_GRID -> {
                val binding = LayoutItemContentsGridBinding.inflate(inflater)
                GridViewHolder(binding)
            }
            else -> {
                val binding = LayoutItemContentsVerticalBinding.inflate(inflater)
                VerticalViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {
        if ( holder is GridViewHolder) {
            holder.bind(mContentItems[p1], p1)
        } else if ( holder is VerticalViewHolder) {
            holder.bind(mContentItems[p1], p1)
        }
    }

    override fun getItemCount(): Int = mContentItems.size


    /*********************************************************************************************
     *
     *    View Holders.
     *
     *********************************************************************************************/
    inner class GridViewHolder(val binding: LayoutItemContentsGridBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ContentItem, position: Int) {
            binding.contentItem = item
        }
    }


    inner class VerticalViewHolder(val binding: LayoutItemContentsVerticalBinding): RecyclerView.ViewHolder(binding.root){
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