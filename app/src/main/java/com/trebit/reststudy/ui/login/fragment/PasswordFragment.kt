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
import com.trebit.reststudy.databinding.LoginFragmentPasswordBinding
import com.trebit.reststudy.ui.BaseFragment
import com.trebit.reststudy.ui.customview.SignUpSuccessDialog
import com.trebit.reststudy.ui.login.activity.LoginActivity
import com.trebit.reststudy.ui.login.viewmodel.LoginViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.login_fragment_password.*
import javax.inject.Inject

/**
 * Jstargram
 * Class: PasswordFragment
 * Created by kangjonghyuk on 04/04/2019.
 *
 * Description:
 */

class PasswordFragment : BaseFragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: LoginViewModel
    private lateinit var mBinding : LoginFragmentPasswordBinding
    private lateinit var mSignUpSuccessDialog : SignUpSuccessDialog

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater          : LayoutInflater,
        container         : ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding  = DataBindingUtil.inflate(inflater, R.layout.login_fragment_password, container, false)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LoginViewModel::class.java)
        mBinding.fragment  = this
        mBinding.activity  = activity as LoginActivity

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextWatcherDouble(img1 = iv_clearMakePw, btn = btn_makePwNext, et1 = et_makePw)
        viewModel.signUpResult.value = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // 계정 생성 Observer.
        viewModel.signUpResult.observe(this, Observer {
            when (it) {
                // 생성 Success
                RES_SUCCESS -> showSignUpSuccessDialog()

                // 생성 Failed
                RES_FAILED -> context?.toast { getString(R.string.desc_sign_up_failed) }
            }
        })
    }

    fun doSignUpFinish(v: View) { viewModel.createUser(et_makePw.text.toString()) }

    //Sign Up Success Dialog Show.
    private fun showSignUpSuccessDialog(){
        mSignUpSuccessDialog = SignUpSuccessDialog(
            ctx   =  activity!!,
            title = getString(R.string.sign_up_success),
            body  = getString(R.string.desc_sign_up_after))
        {
            mSignUpSuccessDialog.dismiss()
            removeFragments()
        }

        mSignUpSuccessDialog.create()
        mSignUpSuccessDialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = PasswordFragment()
    }
}