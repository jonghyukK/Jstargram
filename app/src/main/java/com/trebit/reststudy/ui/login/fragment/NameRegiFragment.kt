package com.trebit.reststudy.ui.login.fragment

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
import com.trebit.reststudy.databinding.FragmentNameRegiBinding
import com.trebit.reststudy.ui.login.activity.LoginActivity
import com.trebit.reststudy.ui.login.viewmodel.LoginViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_name_regi.*
import javax.inject.Inject


/**
 * Rest_study
 * Class: NameRegiFragment
 * Created by kangjonghyuk on 03/04/2019.
 *
 * Description:
 */

class NameRegiFragment : Fragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel : LoginViewModel
    private lateinit var mBinding  : FragmentNameRegiBinding

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {

        mBinding  = DataBindingUtil.inflate(inflater, R.layout.fragment_name_regi, container, false)
        viewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(LoginViewModel::class.java)
        mBinding.viewModel = viewModel
        mBinding.fragment  = this
        mBinding.activity  = activity as LoginActivity

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    fun clearText(v: View){
        viewModel.inputName.value = ""
        et_name.setText("")
    }

    fun nextPage(v: View) {
        mBinding.activity?.addFragment(PasswordFragment.newInstance())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.inputName.postValue("")
    }

    companion object {
        @JvmStatic
        fun newInstance() = NameRegiFragment()
    }
}