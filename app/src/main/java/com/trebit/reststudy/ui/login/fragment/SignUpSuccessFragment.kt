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
import com.trebit.reststudy.databinding.LoginFragmentSignUpSuccessBinding
import com.trebit.reststudy.ui.login.activity.LoginActivity
import com.trebit.reststudy.ui.login.viewmodel.LoginViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.login_fragment_sign_up_success.*
import javax.inject.Inject

/**
 * Jstargram
 * Class: SignUpSuccessFragment
 * Created by kangjonghyuk on 04/04/2019.
 *
 * Description:
 */

class SignUpSuccessFragment : Fragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: LoginViewModel
    private lateinit var mBinding : LoginFragmentSignUpSuccessBinding


    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.login_fragment_sign_up_success,  container, false)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LoginViewModel::class.java)
        mBinding.viewModel = viewModel
        mBinding.fragment = this
        mBinding.activity = activity as LoginActivity

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
    }

    private fun initView(){
        val res = resources
        val registedID = String.format(res.getString(R.string.desc_sign_up_success), viewModel.myName.value)
        tv_signUpDesc.text = registedID
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpSuccessFragment()
    }
}