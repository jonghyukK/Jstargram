package com.trebit.reststudy.ui.login

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trebit.reststudy.R
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_name_regi.*
import javax.inject.Inject


/**
 * Rest_study
 * Class: NameRegiFragment
 * Created by kangjonghyuk on 03/04/2019.
 *
 * Description:
 */

class NameRegiFragment : Fragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel : LoginViewModel
    private lateinit var mBinding  : com.trebit.reststudy.databinding.FragmentNameRegiBinding

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_name_regi, container, false)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LoginViewModel::class.java)
        mBinding.viewModel = viewModel
        mBinding.fragment  = this
        mBinding.activity  = activity as LoginActivity

        return mBinding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.inputName.value = ""
    }

    companion object {
        @JvmStatic
        fun newInstance() = NameRegiFragment()
    }
}