package com.trebit.reststudy.di.module

import com.trebit.reststudy.ui.login.LoginActivity
import com.trebit.reststudy.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * TrebitM-AOS
 * Class: ActivityModule
 * Created by kangjonghyuk on 05/03/2019.
 *
 * Description:
 */

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeLoginActivity(): LoginActivity

}