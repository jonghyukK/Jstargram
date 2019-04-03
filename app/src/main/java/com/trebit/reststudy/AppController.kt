package com.trebit.reststudy

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.trebit.reststudy.di.component.DaggerAppComponent
import com.trebit.reststudy.di.module.ApiModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

/**
 * Rest_study
 * Class: AppController
 * Created by kangjonghyuk on 28/03/2019.
 *
 * Description:
 */

class AppController : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingAndroidInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .application(this)
            .apiModule(ApiModule())
            .build()
            .inject(this)

        // init Log Library
        initLogger()
    }

    /** Init Log Library **/
    private fun initLogger() = Logger.addLogAdapter(AndroidLogAdapter())


}