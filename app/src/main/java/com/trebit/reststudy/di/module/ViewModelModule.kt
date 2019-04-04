package com.trebit.reststudy.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.trebit.reststudy.di.ViewModelFactory
import com.trebit.reststudy.ui.login.viewmodel.LoginViewModel
import com.trebit.reststudy.ui.main.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * TrebitM-AOS
 * Class: ViewModelModule
 * Created by kangjonghyuk on 05/03/2019.
 *
 * Description:
 */

@Module
internal abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    protected abstract fun mainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    protected abstract fun loginViewModel(loginViewModel: LoginViewModel): ViewModel

}