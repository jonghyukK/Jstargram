package com.trebit.reststudy.ui.main.activity

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import com.orhanobut.logger.Logger
import com.trebit.reststudy.*
import com.trebit.reststudy.data.model.UserVo
import com.trebit.reststudy.databinding.ActivityMainBinding
import com.trebit.reststudy.ui.BaseActivity
import com.trebit.reststudy.ui.main.fragment.FirstTabFragment
import com.trebit.reststudy.ui.main.fragment.UserHomeFragment
import com.trebit.reststudy.ui.main.viewmodel.MainViewModel
import com.trebit.reststudy.ui.picture.PictureActivity
import com.yalantis.ucrop.UCrop
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mViewModel   : MainViewModel
    private lateinit var mBinding     : ActivityMainBinding

    private lateinit var firstFrag    : FirstTabFragment
    private lateinit var userHomeFrag : UserHomeFragment

    private var mBackStackArray: MutableList<TabState> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        mBinding   = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        mBinding.viewModel = mViewModel

        initView()

        reqMyAccountInfo()
    }

    private fun initView() {
        bnv_navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bnv_navigationView.menu.findItem(R.id.navi_home).isChecked = true

        firstFrag    = FirstTabFragment.newInstance()
        userHomeFrag = UserHomeFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.fl_mainContainer, firstFrag)
            .add(R.id.fl_mainContainer, userHomeFrag)
            .commit()
        setTabStateFragment(TabState.HOME)
        mBackStackArray.add(TabState.HOME)
    }


    private fun reqMyAccountInfo() {
        val loginEmail = intent.getStringExtra(LOGIN_EMAIL)
        mViewModel.getUser(loginEmail)
    }

    private val mOnNavigationItemSelectedListener
            = BottomNavigationView.OnNavigationItemSelectedListener {

            when (it.itemId) {
                // Home
                R.id.navi_home -> {
                    setTabStateFragment(TabState.HOME)

                    if( mBackStackArray.contains(TabState.HOME))
                        mBackStackArray.remove(TabState.HOME)

                    mBackStackArray.add(TabState.HOME)
                    return@OnNavigationItemSelectedListener true
                }

                // Add Picture
                R.id.navi_add -> startActivityForResult(
                    Intent(this, PictureActivity::class.java), INTENT_PICTURE)

                // My Page
                R.id.navi_mypage -> {
                    setTabStateFragment(TabState.MYPAGE)

                    if( mBackStackArray.contains(TabState.MYPAGE))
                        mBackStackArray.remove(TabState.MYPAGE)

                    mBackStackArray.add(TabState.MYPAGE)
                    return@OnNavigationItemSelectedListener true
                }
            }

        false
    }

    private fun setTabStateFragment(state: TabState) {
        val transaction = supportFragmentManager.beginTransaction()

        when (state) {
            TabState.HOME -> {
                transaction.show(firstFrag)
                transaction.hide(userHomeFrag)
            }

            TabState.MYPAGE -> {
                transaction.hide(firstFrag)
                transaction.show(userHomeFrag)
            }
        }
        transaction.commit()
    }

    override fun onBackPressed() {
        if(mBackStackArray.size == 1) {
            finish()
            return
        }

        val lastIdx = mBackStackArray.size - 1
        mBackStackArray.removeAt(lastIdx)

        when ( mBackStackArray[lastIdx - 1]) {
            TabState.HOME   -> {
                setTabStateFragment(TabState.HOME)
                bnv_navigationView.menu.findItem(R.id.navi_home).isChecked = true
            }

            TabState.MYPAGE -> {
                setTabStateFragment(TabState.MYPAGE)
                bnv_navigationView.menu.findItem(R.id.navi_mypage).isChecked = true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ( resultCode == Activity.RESULT_OK) {
            when (requestCode) {

                // profile Update [ from ProfileEditActivity ]
                INTENT_PROFILE -> {
                    data?.let {
                        val resultData = data.extras.getSerializable(INTENT_PROFILE_DATA) as UserVo
                        mViewModel.myAccountInfo.postValue(resultData)
                    }
                }

                // Image Upload [ from PictureActivity ]
                INTENT_PICTURE -> mViewModel.getContents()
            }
        }
    }

    enum class TabState {
        HOME,
        MYPAGE
    }
}

