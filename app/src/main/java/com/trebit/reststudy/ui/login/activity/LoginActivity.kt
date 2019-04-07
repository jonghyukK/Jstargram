package com.trebit.reststudy.ui.login.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.trebit.reststudy.*
import com.trebit.reststudy.databinding.ActivityLoginBinding
import com.trebit.reststudy.ui.login.fragment.SignUpFragment
import com.trebit.reststudy.ui.login.viewmodel.LoginViewModel
import com.trebit.reststudy.ui.main.activity.MainActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

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

        // Login Observer.
        viewModel.loginResult.observe(this, Observer {
            when(it) {
                // Login Success.
                RES_SUCCESS -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                // Login Failed.
                RES_FAILED -> toast { getString(R.string.desc_login_falied) }
            }
        })
    }

    // Email, Password EditText Clear.
    fun clearText(view: View) {
        when(view.id) {
            // clear email
            R.id.iv_clearEmail -> {
                viewModel.inputEmail.value = ""
                et_email.setText("")
            }
            // clear pw
            R.id.iv_clearPW -> {
                viewModel.inputPw.value = ""
                et_pw.setText("")
            }
        }
    }

    // 로그인 버튼 클릭
    fun doLogin(v: View) {
        viewModel.requestLogin()
    }

    // 가입하기
    fun goSignUpPage(v: View) {
        addFragment(SignUpFragment.newInstance())
    }

    fun addFragment(fragment: Fragment) {
        addFragment(fragment, R.id.rl_container)
    }

    fun removeFragment(v: View) {
        removeFragment()
    }
}
