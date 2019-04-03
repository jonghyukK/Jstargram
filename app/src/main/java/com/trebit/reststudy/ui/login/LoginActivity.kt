package com.trebit.reststudy.ui.login

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.trebit.reststudy.R
import com.trebit.reststudy.addFragment
import com.trebit.reststudy.databinding.ActivityLoginBinding
import com.trebit.reststudy.removeFragment
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_login.*
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
                et_email.requestFocus()
            }
            // clear pw
            R.id.iv_clearPW -> {
                viewModel.inputPw.value = ""
                et_pw.setText("")
                et_pw.requestFocus()
            }
        }
    }

    // Go SignUp Page.
    fun goSignUp(view: View){
        fl_container.visibility = View.VISIBLE
        addFragment(SignUpFragment.newInstance(), R.id.fl_container)
    }

    fun goNameInput(view: View) {
        addFragment(NameRegiFragment.newInstance(), R.id.fl_container)
    }

    fun removeFragment(view: View) {
        removeFragment()
    }




}
