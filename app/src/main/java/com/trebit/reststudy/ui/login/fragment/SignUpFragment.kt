package com.trebit.reststudy.ui.login.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trebit.reststudy.R
import com.trebit.reststudy.databinding.FragmentSignUpBinding
import com.trebit.reststudy.ui.login.activity.LoginActivity
import com.trebit.reststudy.ui.login.viewmodel.LoginViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_sign_up.*
import javax.inject.Inject


class SignUpFragment : Fragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel : LoginViewModel
    private lateinit var mBinding  : FragmentSignUpBinding

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mBinding  = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LoginViewModel::class.java)
        mBinding.viewModel = viewModel
        mBinding.fragment = this
        mBinding.activity = activity as LoginActivity

        return mBinding.root
    }

    fun clearText(v: View){
        viewModel.inputMakeEmail.value = ""
        et_inputMakeEmail.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.inputMakeEmail.postValue("")
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}
