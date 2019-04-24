package com.trebit.reststudy.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.trebit.reststudy.di.ViewModelFactory
import com.trebit.reststudy.ui.login.viewmodel.LoginViewModel
import com.trebit.reststudy.ui.main.viewmodel.MainViewModel
import com.trebit.reststudy.ui.picture.viewModel.PictureViewModel
import com.trebit.reststudy.ui.profile.ProfileViewModel
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


    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    protected abstract fun profileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PictureViewModel::class)
    protected abstract fun pictureViewModel(pictureViewModel: PictureViewModel): ViewModel

}