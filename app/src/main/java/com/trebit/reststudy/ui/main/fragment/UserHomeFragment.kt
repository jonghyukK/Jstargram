package com.trebit.reststudy.ui.main.fragment

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.trebit.reststudy.*
import com.trebit.reststudy.adapter.ItemShowingTypeAdapter
import com.trebit.reststudy.databinding.MainFragmentUserHomeBinding
import com.trebit.reststudy.ui.BaseFragment
import com.trebit.reststudy.ui.login.activity.LoginActivity
import com.trebit.reststudy.ui.main.activity.MainActivity
import com.trebit.reststudy.ui.main.viewmodel.MainViewModel
import com.trebit.reststudy.ui.profile.ProfileEditActivity
import dagger.android.support.AndroidSupportInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.main_fragment_user_home.*
import kotlinx.android.synthetic.main.main_fragment_user_home_content.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Jstargram
 * Class: UserHomeFragment
 * Created by mac on 06/04/2019.
 *
 * Description:
 */

class UserHomeFragment: BaseFragment(), NavigationView.OnNavigationItemSelectedListener {

    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mBinding: MainFragmentUserHomeBinding
    private lateinit var mMainViewModel : MainViewModel

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater          : LayoutInflater,
                              container         : ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding       = DataBindingUtil.inflate(inflater, R.layout.main_fragment_user_home, container, false)
        mMainViewModel = ViewModelProviders.of(activity!!, viewModelFactory).get(MainViewModel::class.java)
        mBinding.mainActivity     = activity as MainActivity
        mBinding.mainViewModel    = mMainViewModel
        mBinding.userHomeFragment = this

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tab Layout Init.
        initTabLayout()
    }


    // TabBar Layout Settings.
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

        // NavigationView Init.
        val toggle = ActionBarDrawerToggle(activity, dl_mainDrawerRoot, toolbar, R.string.drawer_open, R.string.drawer_close)
        dl_mainDrawerRoot.addDrawerListener(toggle)
        nv_navView.setNavigationItemSelectedListener(this)
    }

    // go Profile Edit Activity.
    fun goEditProfile(v: View){
        activity?.startActivityForResult(
            Intent(activity, ProfileEditActivity::class.java), INTENT_PROFILE)
    }


    // Event For ToolBar Btn.
    fun onClickToolBarBtn(v: View){
        if (dl_mainDrawerRoot.isDrawerOpen(GravityCompat.END)){
            dl_mainDrawerRoot.closeDrawer(GravityCompat.END)
        } else {
            dl_mainDrawerRoot.openDrawer(GravityCompat.END)
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // Logout
            R.id.nav_logout -> {
                mPref.putData(PREF_CHECKED_AUTO_LOGIN, false)
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
            }
        }
        dl_mainDrawerRoot.closeDrawer(GravityCompat.END)
        return true
    }


    companion object {
        @JvmStatic
        fun newInstance() = UserHomeFragment()
    }
}