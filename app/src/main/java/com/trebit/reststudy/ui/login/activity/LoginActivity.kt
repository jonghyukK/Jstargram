package com.trebit.reststudy.ui.login.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.trebit.reststudy.*
import com.trebit.reststudy.databinding.ActivityLoginBinding
import com.trebit.reststudy.ui.BaseActivity
import com.trebit.reststudy.ui.login.fragment.SignUpFragment
import com.trebit.reststudy.ui.login.viewmodel.LoginViewModel
import com.trebit.reststudy.ui.main.activity.MainActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mLoginViewModel: LoginViewModel
    private lateinit var mBinding       : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        mBinding  = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mLoginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        mBinding.viewModel = mLoginViewModel
        mBinding.activity  = this

        init()
    }

    private fun init() {
        addTextWatcherDouble(iv_clearEmail, iv_clearPW, btn_login, et_email, et_pw)

        // init Email Text.
        et_email.setText(mPref.getPrefEmail(PREF_EMAIL))

        // Login Observer.
        mLoginViewModel.loginResult.observe(this, Observer {
            when(it?.resCode) {
                // Login Success.
                RES_SUCCESS -> startMainActivity()
                // Login Failed.
                RES_FAILED -> toast { getString(R.string.desc_login_falied) }
            }
        })
    }

    private fun startMainActivity() {
        // Email to Pref
        mPref.putData(PREF_EMAIL, et_email.text.toString())

        // 자동 로그인 Check가 되어 있으면 Save.
        if (cb_autoLogin.isChecked) {
            mPref.putData(PREF_PW, et_pw.text.toString())
            mPref.putData(PREF_AUTO_LOGIN, true)
        } else {
            mPref.putData(PREF_AUTO_LOGIN, false)
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra(LOGIN_EMAIL, et_email.text.toString())
        startActivity(intent)
        finish()
    }

    // do Login,
    fun requestLogin(v: View) {
        val inputEmail = et_email.text.toString()
        val inputPassW = et_pw.text.toString()

        mLoginViewModel.requestLogin(inputEmail, inputPassW)
    }

    // do Sign Up.
    fun reqSignUp(v: View) = addFragment(SignUpFragment.newInstance(), R.id.rl_container)

    // Remove Fragments.
    fun removeFragment(v: View) {
        removeFragment()
    }

}
