package com.trebit.reststudy.ui.login.activity

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.trebit.reststudy.R
import com.trebit.reststudy.addFragment
import com.trebit.reststudy.databinding.ActivityLoginBinding
import com.trebit.reststudy.removeFragment
import com.trebit.reststudy.ui.login.viewmodel.LoginViewModel
import com.trebit.reststudy.ui.login.fragment.NameRegiFragment
import com.trebit.reststudy.ui.login.fragment.PasswordFragment
import com.trebit.reststudy.ui.login.fragment.SignUpFragment
import com.trebit.reststudy.ui.login.fragment.SignUpSuccessFragment
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_name_regi.*
import kotlinx.android.synthetic.main.fragment_password.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
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

    fun navigatePage(view: View) {
        addFragment( when(view.id) {
            R.id.tv_signup      -> SignUpFragment.newInstance()
            R.id.btn_next       -> NameRegiFragment.newInstance()
            R.id.btn_nameNext   -> PasswordFragment.newInstance()
            R.id.btn_makePwNext -> SignUpSuccessFragment.newInstance()
            else -> return
        }, R.id.rl_container)
    }

    fun removeFragment(v: View) {
        removeFragment()
    }
}
