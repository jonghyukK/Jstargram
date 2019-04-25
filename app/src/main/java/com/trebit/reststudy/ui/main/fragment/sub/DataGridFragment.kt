package com.trebit.reststudy.ui.main.fragment.sub

import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import com.trebit.reststudy.R
import com.trebit.reststudy.databinding.FragmentDataGridBinding
import com.trebit.reststudy.ui.BaseFragment
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Jstargram
 * Class: DataGridFragment
 * Created by kangjonghyuk on 08/04/2019.
 *
 * Description:
 */

class DataGridFragment : BaseFragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mBinding: FragmentDataGridBinding

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_data_grid, container, false)

        Logger.d("Grid Fragment")
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() = DataGridFragment()
    }
}