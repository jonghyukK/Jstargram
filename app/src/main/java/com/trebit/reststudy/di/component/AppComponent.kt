package com.trebit.reststudy.di.component

import android.app.Application
import com.trebit.reststudy.AppController
import com.trebit.reststudy.di.module.ActivityModule
import com.trebit.reststudy.di.module.ApiModule
import com.trebit.reststudy.di.module.FragmentModule
import com.trebit.reststudy.di.module.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * TrebitM-AOS
 * Class: AppComponent
 * Created by kangjonghyuk on 05/03/2019.
 *
 * Description:
 */

@Singleton
@Component(modules = [
    ApiModule::class,
    ActivityModule::class,
    FragmentModule::class,
    ViewModelModule::class,
    AndroidSupportInjectionModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        @BindsInstance
        fun apiModule(apiModule: ApiModule): Builder

        fun build(): AppComponent
    }

    fun inject(appController: AppController)
}