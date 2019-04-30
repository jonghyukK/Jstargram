package com.trebit.reststudy.ui.main.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import com.trebit.reststudy.R
import com.trebit.reststudy.adapter.ContentsAdapter
import com.trebit.reststudy.databinding.MainFragmentFirstTabBinding
import com.trebit.reststudy.ui.BaseFragment
import com.trebit.reststudy.ui.main.activity.MainActivity
import com.trebit.reststudy.ui.main.viewmodel.MainViewModel
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

class FirstTabFragment: BaseFragment(){

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
        mContentsAdapter = ContentsAdapter()
        rv_contentList.setHasFixedSize(true)
        rv_contentList.adapter = mContentsAdapter

        mMainViewModel.getContents()
    }


    companion object {
        @JvmStatic
        fun newInstance() = FirstTabFragment()
    }
}