package com.trebit.reststudy.di.module

import com.trebit.reststudy.ui.login.fragment.NameRegiFragment
import com.trebit.reststudy.ui.login.fragment.PasswordFragment
import com.trebit.reststudy.ui.login.fragment.SignUpFragment
import com.trebit.reststudy.ui.login.fragment.SignUpSuccessFragment
import com.trebit.reststudy.ui.main.fragment.FirstTabFragment
import com.trebit.reststudy.ui.main.fragment.SecondTabFragment
import com.trebit.reststudy.ui.main.fragment.ThirdTabFragment
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

    @ContributesAndroidInjector
    abstract fun contributeNameRegiFragment(): NameRegiFragment

    @ContributesAndroidInjector
    abstract fun contributePasswordFragment(): PasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeSignUpSuccessFragment(): SignUpSuccessFragment



    /*********************************************************************
     *
     *   Fragments in MainActivity.
     *
     *********************************************************************/
    @ContributesAndroidInjector
    abstract fun contributeFirstTabFragment(): FirstTabFragment

    @ContributesAndroidInjector
    abstract fun contributeSecondTabFragment(): SecondTabFragment

    @ContributesAndroidInjector
    abstract fun contributeThirdTabFragment(): ThirdTabFragment
}