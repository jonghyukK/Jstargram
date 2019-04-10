package com.trebit.reststudy.ui.main.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import com.trebit.reststudy.R
import com.trebit.reststudy.data.model.UserVo
import com.trebit.reststudy.databinding.FragmentProfileEditBinding
import com.trebit.reststudy.ui.main.viewmodel.MainViewModel
import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import javax.inject.Inject

/**
 * Jstargram
 * Class: ProfileEditFragment
 * Created by kangjonghyuk on 10/04/2019.
 *
 * Description:
 */

class ProfileEditFragment: DialogFragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mMainViewModel : MainViewModel
    private lateinit var mBinding : FragmentProfileEditBinding
    private lateinit var mMyAccountData : UserVo
    private val compositeDisposable by lazy { CompositeDisposable() }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen)
    }

    override fun onCreateView(inflater           : LayoutInflater,
                              container          : ViewGroup?,
                              savedInstanceState : Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_edit, container, false)
        mMainViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)
        mBinding.mainViewModel = mMainViewModel
        mBinding.fragment      = this

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMyAccountData = mMainViewModel.myAccountInfo.value!!

        et_editName.setText(mMyAccountData.name)
        et_editIntroduce.setText(mMyAccountData.introduce)

    }

    fun onClickEvent(v: View) {
        when(v.id) {
            R.id.tv_cancel -> dismiss()
            R.id.tv_doFinish -> {
                compositeDisposable.add(
                mBinding.mainViewModel?.updateUser(
                    email = mMyAccountData.email,
                    name = et_editName.text.toString(),
                    introduce = et_editIntroduce.text.toString(),
                    profile_img = "str")
                    !!.subscribe({
                    Logger.d("TTT")
                }, {
                    Logger.d("error TTT")
                }))
            }
            R.id.tv_editProfileImg -> {}
            R.id.iv_clearName -> {}
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileEditFragment()
    }

}