package com.trebit.reststudy.ui.login.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.trebit.reststudy.*
import com.trebit.reststudy.databinding.ActivityLoginBinding
import com.trebit.reststudy.ui.BaseActivity
import com.trebit.reststudy.ui.login.fragment.SignUpFragment
import com.trebit.reststudy.ui.login.viewmodel.LoginViewModel
import com.trebit.reststudy.ui.main.activity.MainActivity
import com.trebit.reststudy.utils.SharedPref
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: LoginViewModel
    private lateinit var mBinding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        mBinding  = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        mBinding.viewModel = viewModel
        mBinding.activity  = this

        init()
    }

    private fun init() {

        addTextWatcherDouble(iv_clearEmail, iv_clearPW, btn_login, et_email, et_pw)

        // 자동 로그인 상태 (o)                                                                                                                                                                                                                                     c
        if (mPref.isAutoLogin(PREF_CHECKED_AUTO_LOGIN)) {
            et_email.setText(mPref.getPrefEmail(PREF_EMAIL))
            et_pw   .setText(mPref.getPrefPw(PREF_PW))
            cb_autoLogin.isChecked = true
        }

        // Login Observer.
        viewModel.loginResult.observe(this, Observer {
            when(it) {
                // Login Success.
                RES_SUCCESS -> {
                    saveLoginInfo()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(LOGIN_EMAIL, et_email.text.toString())
                    startActivity(intent)
                    finish()
                }
                // Login Failed.
                RES_FAILED -> toast { getString(R.string.desc_login_falied) }
            }
        })
    }

    private fun saveLoginInfo() {
        // Email to Pref
        mPref.putData(PREF_EMAIL, et_email.text.toString())

        // PW to Pref
        mPref.putData(PREF_PW, et_pw.text.toString())

        // Auto Login to Pref
        mPref.putData(PREF_CHECKED_AUTO_LOGIN, cb_autoLogin.isChecked)
    }


    fun onClickEvent(v: View) {
        when ( v.id ) {
            // Login
            R.id.btn_login -> viewModel.requestLogin(et_email.text.toString(), et_pw.text.toString())
            // Sign Up
            R.id.tv_signup -> addFragment(SignUpFragment.newInstance())
        }
    }

    fun addFragment(fragment: Fragment) {
        addFragment(fragment, R.id.rl_container)
    }

    fun removeFragment(v: View) {
        removeFragment()
    }

}
