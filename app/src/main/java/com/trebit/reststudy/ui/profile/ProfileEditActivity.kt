package com.trebit.reststudy.ui.profile

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.trebit.reststudy.*
import com.trebit.reststudy.databinding.ActivityProfileEditBinding
import com.trebit.reststudy.ui.BaseActivity
import com.trebit.reststudy.ui.main.viewmodel.MainViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_profile_edit.*
import javax.inject.Inject

/**
 * Jstargram
 * Class: ProfileEditActivity
 * Created by kangjonghyuk on 11/04/2019.
 *
 * Description:
 */

class ProfileEditActivity: BaseActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mViewModel     : ProfileViewModel
    private lateinit var mMainViewModel : MainViewModel
    private lateinit var mBinding       : ActivityProfileEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        mBinding    = DataBindingUtil.setContentView(this, R.layout.activity_profile_edit)
        mViewModel  = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        mMainViewModel         = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        mBinding.viewModel     = mViewModel
        mBinding.mainViewModel = mMainViewModel
        mBinding.activity      = this

        init()
    }

    private fun init() {
        et_editName.addTextWatcher(iv_clearEditName)

        mViewModel.myProfileInfo.observe(this, Observer { value ->
            value?.let {
                val intent = Intent()
                intent.putExtra(INTENT_PROFILE_DATA, value)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        })
    }


    fun onClickEvent(v: View) {
        when(v.id) {
            // 취소
            R.id.tv_cancel         -> finish()
            // 완료
            R.id.tv_doFinish       -> updateUser()
            // 프로필 사진 바꾸기
            R.id.tv_editProfileImg -> {}
        }
    }


    private fun updateUser() {
        if (et_editName.text.isEmpty()) {
            toast { getString(R.string.desc_input_name) }
            return
        }

        mViewModel.updateUser(
            email       = mPref.getPrefEmail(PREF_EMAIL),
            name        = et_editName     .text.toString(),
            introduce   = et_editIntroduce.text.toString(),
            profile_img = "profile_path")
    }
}