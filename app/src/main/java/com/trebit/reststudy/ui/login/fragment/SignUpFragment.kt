package com.trebit.reststudy.ui.login.fragment

import android.app.AlertDialog
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trebit.reststudy.*
import com.trebit.reststudy.databinding.LoginFragmentSignUpBinding
import com.trebit.reststudy.ui.BaseFragment
import com.trebit.reststudy.ui.customview.SignUpSuccessDialog
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

    private lateinit var viewModel : LoginViewModel
    private lateinit var mBinding  : LoginFragmentSignUpBinding
    private lateinit var mSignUpDialog : SignUpSuccessDialog

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
        et_inputMakeEmail.addTextWatcher(mBinding.ivClearEmail)
        et_inputMakePw   .addTextWatcher(mBinding.ivClearPw)
        et_inputMakeName .addTextWatcher(mBinding.ivClearName)

        viewModel.signUpResult.value = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.signUpResult.observe(this, Observer {
            when (it?.resCode) {
                // success.
                RES_SUCCESS -> signUpSuccessDialog()
                // failed.
                RES_FAILED -> activity?.toast { it.resMsg }
            }
        })
    }

    private fun signUpSuccessDialog(){
        mSignUpDialog = SignUpSuccessDialog(
            ctx   = activity!!,
            title = getString(R.string.sign_up_success),
            body  = getString(R.string.desc_sign_up_after)){
            mSignUpDialog.dismiss()
            removeFragments()
            }
        mSignUpDialog.show()
    }

    // validate Email.
    fun checkInputData(v: View) {
        val inputedEmail : String = et_inputMakeEmail.text.toString()
        val inputedPW    : String = et_inputMakePw   .text.toString()
        val inputedName  : String = et_inputMakeName .text.toString()

        if ( inputedEmail.isEmpty() || inputedPW.isEmpty() || inputedName.isEmpty()) {
            AlertDialog.Builder(activity)
                .setMessage(getString(R.string.desc_signup_error))
                .setPositiveButton(getString(R.string.confirm)) { _, _ -> }
                .show()
            return
        }

        viewModel.reqSignUp(inputedEmail, inputedPW, inputedName)
    }



    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}
