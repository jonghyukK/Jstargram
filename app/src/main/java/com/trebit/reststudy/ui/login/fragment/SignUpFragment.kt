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
import com.trebit.reststudy.R
import com.trebit.reststudy.addFragment
import com.trebit.reststudy.addTextWatcherDouble
import com.trebit.reststudy.databinding.LoginFragmentSignUpBinding
import com.trebit.reststudy.ui.BaseFragment
import com.trebit.reststudy.ui.login.activity.LoginActivity
import com.trebit.reststudy.ui.login.viewmodel.LoginViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.login_fragment_sign_up.*
import javax.inject.Inject

/**
 * Rest_study
 * Class: SignUpFragment
 * Created by kangjonghyuk on 03/04/2019.
 *
 * Description:
 */

class SignUpFragment : BaseFragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private val VALID_EMAIL   = "Y"
    private val INVALID_EMAIL = "N"
    private lateinit var viewModel : LoginViewModel
    private lateinit var mBinding  : LoginFragmentSignUpBinding

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater          : LayoutInflater,
        container         : ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding  = DataBindingUtil.inflate(inflater, R.layout.login_fragment_sign_up, container, false)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LoginViewModel::class.java)
        mBinding.fragment  = this
        mBinding.activity  = activity as LoginActivity

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addTextWatcherDouble(img1 = iv_clearValue, btn  = btn_next, et1  = et_inputMakeEmail)
        viewModel.isValidEmail.value = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Email Validate Observer.
        viewModel.isValidEmail.observe(this, Observer {
            when (it) {
                // 사용 가능 Email.
                VALID_EMAIL -> {
                    tv_alreadyRegiEmail.visibility = View.GONE
                    viewModel.myEmail.value = et_inputMakeEmail.text.toString()
                    addFragment(NameRegiFragment.newInstance(), R.id.rl_container)
                }
                // 사용 불가 Email.
                INVALID_EMAIL -> tv_alreadyRegiEmail.visibility = View.VISIBLE
            }
        })
    }

    // validate Email.
    fun validateEmail(v: View) {
        viewModel.validateEmail(et_inputMakeEmail.text.toString())
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}
