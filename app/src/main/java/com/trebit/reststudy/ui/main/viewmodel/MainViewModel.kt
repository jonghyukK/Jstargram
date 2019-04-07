package com.trebit.reststudy.ui.main.viewmodel

import android.arch.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.trebit.reststudy.data.remote.ApiService
import com.trebit.reststudy.data.repository.DataRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Rest_study
 * Class: MainViewModel
 * Created by kangjonghyuk on 28/03/2019.
 *
 * Description:
 */

class MainViewModel @Inject constructor(
    apiService: ApiService
): ViewModel(){

    private val compositeDisposable by lazy { CompositeDisposable() }
    private val repository by lazy { DataRepository(apiService) }

}