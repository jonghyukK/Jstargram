package com.trebit.reststudy.ui.main

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.trebit.reststudy.R
import com.trebit.reststudy.databinding.ActivityMainBinding
import com.trebit.reststudy.ui.login.activity.LoginActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mViewModel: MainViewModel
    private lateinit var mBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        mBinding.viewModel = mViewModel

//        showAllContents()

        startActivity(Intent(this, LoginActivity::class.java))
    }

    private fun showAllContents(){
        mViewModel.getAllContents()

        mViewModel.createContents()
    }
}
