package com.trebit.reststudy.ui.login.fragment

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
import com.trebit.reststudy.*
import com.trebit.reststudy.databinding.FragmentPasswordBinding
import com.trebit.reststudy.ui.login.activity.LoginActivity
import com.trebit.reststudy.ui.login.viewmodel.LoginViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_password.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import javax.inject.Inject

/**
 * Jstargram
 * Class: PasswordFragment
 * Created by kangjonghyuk on 04/04/2019.
 *
 * Description:
 */

class PasswordFragment : Fragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: LoginViewModel
    private lateinit var mBinding : FragmentPasswordBinding

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBinding  = DataBindingUtil.inflate(inflater, R.layout.fragment_password, container, false)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LoginViewModel::class.java)
        mBinding.viewModel = viewModel
        mBinding.fragment  = this
        mBinding.activity  = activity as LoginActivity

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.signUpResult.observe(this, Observer {
            when(it) {
                RES_SUCCESS -> mBinding.activity?.addFragment(SignUpSuccessFragment.newInstance())
                RES_FAILED  -> context?.toast { getString(R.string.desc_sign_up_failed) }
            }
        })
    }

    fun clearText(v: View) {
        viewModel.inputMakePW.value = ""
        et_makePw.setText("")
    }

    fun doSignUpFinish(v: View) {
        viewModel.createUser()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.inputMakePW.postValue("")
        viewModel.signUpResult.postValue("")
    }

    companion object {
        @JvmStatic
        fun newInstance() = PasswordFragment()
    }
}