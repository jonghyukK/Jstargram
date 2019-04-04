package com.trebit.reststudy.di.module

import com.trebit.reststudy.ui.login.fragment.NameRegiFragment
import com.trebit.reststudy.ui.login.fragment.PasswordFragment
import com.trebit.reststudy.ui.login.fragment.SignUpFragment
import com.trebit.reststudy.ui.login.fragment.SignUpSuccessFragment
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

    @ContributesAndroidInjector
    abstract fun contributeSignUpFragment(): SignUpFragment

    @ContributesAndroidInjector
    abstract fun contributeNameRegiFragment(): NameRegiFragment

    @ContributesAndroidInjector
    abstract fun contributePasswordFragment(): PasswordFragment

    @ContributesAndroidInjector
    abstract fun contributeSignUpSuccessFragment(): SignUpSuccessFragment
}