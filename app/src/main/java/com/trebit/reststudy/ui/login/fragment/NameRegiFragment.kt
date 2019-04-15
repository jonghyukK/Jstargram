package com.trebit.reststudy.ui.login.fragment

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
import com.trebit.reststudy.addFragment
import com.trebit.reststudy.addTextWatcherDouble
import com.trebit.reststudy.databinding.LoginFragmentNameRegiBinding
import com.trebit.reststudy.ui.BaseFragment
import com.trebit.reststudy.ui.login.activity.LoginActivity
import com.trebit.reststudy.ui.login.viewmodel.LoginViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.login_fragment_name_regi.*
import javax.inject.Inject


/**
 * Rest_study
 * Class: NameRegiFragment
 * Created by kangjonghyuk on 03/04/2019.
 *
 * Description:
 */

class NameRegiFragment : BaseFragment(){

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel : LoginViewModel
    private lateinit var mBinding  : LoginFragmentNameRegiBinding

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater          : LayoutInflater,
        container         : ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding  = DataBindingUtil.inflate(inflater, R.layout.login_fragment_name_regi, container, false)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LoginViewModel::class.java)
        mBinding.fragment  = this
        mBinding.activity  = activity as LoginActivity

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextWatcherDouble(img1 = iv_clearName, btn = btn_nameNext, et1 = et_name)
        viewModel.myName.value = null
    }

    fun nextPage(v: View) {
        viewModel.myName.value = et_name.text.toString()
        addFragment(PasswordFragment.newInstance(), R.id.rl_container)
    }

    companion object {
        @JvmStatic
        fun newInstance() = NameRegiFragment()
    }
}