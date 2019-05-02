package com.trebit.reststudy.ui.main.fragment

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.orhanobut.logger.Logger
import com.trebit.reststudy.R
import com.trebit.reststudy.adapter.ContentsAdapter
import com.trebit.reststudy.data.model.ContentItem
import com.trebit.reststudy.databinding.MainFragmentFirstTabBinding
import com.trebit.reststudy.ui.BaseFragment
import com.trebit.reststudy.ui.main.activity.MainActivity
import com.trebit.reststudy.ui.main.viewmodel.MainViewModel
import com.trebit.reststudy.utils.ItemClickSupport
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.main_fragment_first_tab.*
import javax.inject.Inject


/**
 * Jstargram
 * Class: FirstTabFragment
 * Created by mac on 05/04/2019.
 *
 * Description:
 */

class FirstTabFragment: BaseFragment(), ContentsAdapter.ContentEventListener {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mBinding         : MainFragmentFirstTabBinding
    private lateinit var mMainViewModel   : MainViewModel
    private lateinit var mContentsAdapter : ContentsAdapter

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater          : LayoutInflater,
                              container         : ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        mBinding       = DataBindingUtil.inflate(inflater, R.layout.main_fragment_first_tab, container, false)
        mMainViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        mBinding.viewModel = mMainViewModel
        mBinding.activity  = activity as MainActivity

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initRecyclerView()

        mMainViewModel.allContents.observe(this, Observer {
            it?.let { mContentsAdapter.setContentItem(it) }
        })
    }

    private fun initRecyclerView(){
        srl_swipeLayout.setOnRefreshListener {
            mMainViewModel.getContents()
            srl_swipeLayout.isRefreshing = false
        }

        mContentsAdapter = ContentsAdapter().apply { mContentEventListener = this@FirstTabFragment }
        val lm = LinearLayoutManager(activity).apply {
            reverseLayout = true
            stackFromEnd  = true
        }
        with(rv_contentList) {
            setHasFixedSize(true)
            layoutManager = lm
            adapter = mContentsAdapter
        }

        ItemClickSupport.addTo(rv_contentList)
            .setOnItemClickListener(object: ItemClickSupport.OnItemClickListener {
                // Single Clicked.
                override fun onItemClicked(recyclerView: RecyclerView?, position: Int, v: View?) {
                    Logger.d("clicked : $position")
                }

                // Double Clicked.
                override fun onItemDoubleClicked(recyclerView: RecyclerView?, position: Int, v: View?) {
                    Logger.d("double Clicked")
                    val tv = v?.findViewById<TextView>(R.id.tv_favoriteInfo)
                    tv?.visibility = if (tv?.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }
            })

        mMainViewModel.getContents()
    }

    override fun clickedMore(item: ContentItem, pos: Int) {
        val view = layoutInflater.inflate(R.layout.dialog_content_more, null)
        val tvRmContent = view.findViewById<TextView>(R.id.tv_removeContent)

        val builder  = AlertDialog.Builder(activity)
            .setView(view)
            .create()
        builder.show()

        tvRmContent.setOnClickListener {
            builder.dismiss()
            mMainViewModel.removeContent(item.contents_id)
            mContentsAdapter.updateContentItem(item, pos)

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FirstTabFragment()
    }
}