package com.trebit.reststudy.ui.main.activity

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.trebit.reststudy.R
import com.trebit.reststudy.addFragment
import com.trebit.reststudy.databinding.ActivityMainBinding
import com.trebit.reststudy.replaceFragment
import com.trebit.reststudy.replaceFragmentNotStack
import com.trebit.reststudy.ui.main.fragment.FirstTabFragment
import com.trebit.reststudy.ui.main.fragment.SecondTabFragment
import com.trebit.reststudy.ui.main.fragment.ThirdTabFragment
import com.trebit.reststudy.ui.main.viewmodel.MainViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        mBinding   = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        mBinding.viewModel = mViewModel

        init()
    }

    private fun init() {
        // Set First Fragment.
        replaceFragmentNotStack(FirstTabFragment.newInstance(), R.id.fl_mainContainer)
        // set Bottom Navigation Event Listener.
        bnv_navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    private val mOnNavigationItemSelectedListener
            = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navi_home -> {
                replaceFragment(FirstTabFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }

            R.id.navi_add -> {
                replaceFragment(SecondTabFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }

            R.id.navi_mypage -> {
                replaceFragment(ThirdTabFragment.newInstance())
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun addFragment(frag: Fragment){
        addFragment(frag, R.id.fl_mainContainer)
    }

    fun replaceFragment(frag: Fragment){
        replaceFragment(frag, R.id.fl_mainContainer)
    }


}
