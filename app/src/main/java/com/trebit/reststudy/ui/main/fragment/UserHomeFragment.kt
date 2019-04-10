package com.trebit.reststudy.ui.main.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import com.trebit.reststudy.R
import com.trebit.reststudy.adapter.ItemShowingTypeAdapter
import com.trebit.reststudy.addFragment
import com.trebit.reststudy.databinding.MainSubFragmentUserHomeBinding
import com.trebit.reststudy.ui.main.activity.MainActivity
import com.trebit.reststudy.ui.main.viewmodel.MainViewModel
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.main_sub_fragment_user_home.*
import javax.inject.Inject

/**
 * Jstargram
 * Class: UserHomeFragment
 * Created by mac on 06/04/2019.
 *
 * Description:
 */

class UserHomeFragment: Fragment() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mBinding: MainSubFragmentUserHomeBinding
    private lateinit var mMainViewModel : MainViewModel

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater          : LayoutInflater,
                              container         : ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding       = DataBindingUtil.inflate(inflater, R.layout.main_sub_fragment_user_home, container, false)
        mMainViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)
        mBinding.mainActivity     = activity as MainActivity
        mBinding.mainViewModel    = mMainViewModel
        mBinding.userHomeFragment = this


        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        Logger.d("UserHomeFragment onActivityCreated")
        initTabLayout()
    }


    private fun initTabLayout() {
        tl_tabLayout.addTab(tl_tabLayout.newTab().setIcon(R.drawable.icon_item_grid_b))
        tl_tabLayout.addTab(tl_tabLayout.newTab().setIcon(R.drawable.icon_item_vertical_b))

        val adapter = ItemShowingTypeAdapter(fragmentManager!!, tl_tabLayout.tabCount)
        vp_viewPager.adapter = adapter

        vp_viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tl_tabLayout))
        tl_tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                vp_viewPager.currentItem = p0!!.position
            }
        })
    }

    fun goEditProfile(v: View){
        mBinding.mainActivity?.showProfileDialogFrag(ProfileEditFragment.newInstance())
    }

    companion object {
        @JvmStatic
        fun newInstance() = UserHomeFragment()
    }
}