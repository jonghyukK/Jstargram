package com.trebit.reststudy.ui.main.fragment

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trebit.reststudy.R
import com.trebit.reststudy.databinding.MainFragmentSecondTabBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Jstargram
 * Class: SecondTabFragment
 * Created by mac on 05/04/2019.
 *
 * Description:
 */

class SecondTabFragment: Fragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mBinding: MainFragmentSecondTabBinding

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater          : LayoutInflater,
                              container         : ViewGroup?,
                              savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment_second_tab, container, false)

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = SecondTabFragment()
    }

}