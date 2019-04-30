package com.trebit.reststudy.di.module

import com.trebit.reststudy.ui.login.fragment.SignUpFragment
import com.trebit.reststudy.ui.main.fragment.FirstTabFragment
import com.trebit.reststudy.ui.main.fragment.sub.DataGridFragment
import com.trebit.reststudy.ui.main.fragment.sub.DataVerticalFragment
import com.trebit.reststudy.ui.main.fragment.UserHomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Rest_study
 * Class: FragmentModule
 * Created by kangjonghyuk on 03/04/2019.
 *
 * Description:
 */

@Module
abstract class FragmentModule {


    /*********************************************************************
     *
     *   Fragments in LoginActivity.
     *
     *********************************************************************/
    @ContributesAndroidInjector
    abstract fun contributeSignUpFragment(): SignUpFragment

    /*********************************************************************
     *
     *   Fragments in MainActivity.
     *
     *********************************************************************/
    @ContributesAndroidInjector
    abstract fun contributeFirstTabFragment(): FirstTabFragment




    /*********************************************************************
     *
     *   Sub Fragments ...
     *
     *********************************************************************/
    @ContributesAndroidInjector
    abstract fun contributeUserHomeFragment(): UserHomeFragment

    @ContributesAndroidInjector
    abstract fun contributeDataGridFragment(): DataGridFragment

    @ContributesAndroidInjector
    abstract fun contributeDataVerticalFragment(): DataVerticalFragment

}