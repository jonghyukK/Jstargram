package com.trebit.reststudy.ui.login

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


    // Tab Event for PhoneNumber or Email.
    fun selectTab(view: View) {
        val isFirstTab = view.id == R.id.rl_pNumber
        val selectedColor = resources.getColor(R.color.black)
        val unselectedColor = resources.getColor(R.color.gray_3)

        tv_pNumber.setTextColor      ( if (isFirstTab) selectedColor   else unselectedColor)
        tv_email  .setTextColor      ( if (isFirstTab) unselectedColor else selectedColor)
        rl_pNumber.setBackgroundColor( if (isFirstTab) selectedColor   else unselectedColor)
        rl_email  .setBackgroundColor( if (isFirstTab) unselectedColor else selectedColor)

        et_inputValue.hint = if (isFirstTab) getString(R.string.phone_number) else getString(R.string.email_addr)
        et_inputValue.inputType =
            if (isFirstTab) InputType.TYPE_CLASS_PHONE else InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        et_inputValue.setText("")

        viewModel.inputValue.value = ""
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.inputValue.value = ""
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignUpFragment()
    }
}
