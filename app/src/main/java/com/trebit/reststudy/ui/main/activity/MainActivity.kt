package com.trebit.reststudy.ui.main.activity

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.content.ContextCompat
import com.orhanobut.logger.Logger
import com.trebit.reststudy.INTENT_PROFILE
import com.trebit.reststudy.INTENT_PROFILE_DATA
import com.trebit.reststudy.LOGIN_EMAIL
import com.trebit.reststudy.R
import com.trebit.reststudy.data.model.UserVo
import com.trebit.reststudy.databinding.ActivityMainBinding
import com.trebit.reststudy.ui.BaseActivity
import com.trebit.reststudy.ui.main.fragment.FirstTabFragment
import com.trebit.reststudy.ui.main.fragment.SecondTabFragment
import com.trebit.reststudy.ui.main.fragment.UserHomeFragment
import com.trebit.reststudy.ui.main.viewmodel.MainViewModel
import com.trebit.reststudy.utils.FileUtils
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
    private lateinit var secondFrag   : SecondTabFragment
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
        secondFrag   = SecondTabFragment.newInstance()
        userHomeFrag = UserHomeFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.fl_mainContainer, firstFrag)
            .add(R.id.fl_mainContainer, secondFrag)
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
                R.id.navi_add -> {
                    setTabStateFragment(TabState.ADD)

                    if( mBackStackArray.contains(TabState.ADD))
                        mBackStackArray.remove(TabState.ADD)

                    mBackStackArray.add(TabState.ADD)
                    return@OnNavigationItemSelectedListener true
                }

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
                transaction.hide(secondFrag)
                transaction.hide(userHomeFrag)
            }
            TabState.ADD -> {
                transaction.hide(firstFrag)
                transaction.show(secondFrag)
                transaction.hide(userHomeFrag)
            }
            TabState.MYPAGE -> {
                transaction.hide(firstFrag)
                transaction.hide(secondFrag)
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
            TabState.ADD    -> {
                setTabStateFragment(TabState.ADD)
                bnv_navigationView.menu.findItem(R.id.navi_add).isChecked = true
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

                100 -> {
                    val sourceUri = data?.data
                    val file = FileUtils.createImageFile()
                    val destinationUri = Uri.fromFile(file)
                    openCropActivity(sourceUri!!, destinationUri)


                    Logger.d("""
                        sourceUri : $sourceUri
                        desUri : $destinationUri
                    """.trimIndent())
                }

                UCrop.REQUEST_CROP -> {
                    val uri = UCrop.getOutput(data!!)
                    Logger.d("""
                        uri : $uri
                        uriPath : ${uri?.path}
                    """.trimIndent())
                }
            }
        }
    }

    private fun openCropActivity(sourceUri: Uri, destinationUri: Uri) {
        val options = UCrop.Options()
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent))
        options.setMaxBitmapSize(10000)

        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(5f, 5f)
            .withOptions(options)
            .start(this)
    }


    enum class TabState {
        HOME,
        ADD,
        MYPAGE
    }


}

