package com.trebit.reststudy.ui.login.viewmodel

import android.arch.lifecycle.MutableLiveData
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
 * Class: LoginViewModel
 * Created by kangjonghyuk on 28/03/2019.
 *
 * Description:
 */

class LoginViewModel @Inject constructor(
    apiService: ApiService
): ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }
    private val repository by lazy { DataRepository(apiService) }

    val inputEmail     : MutableLiveData<String> = MutableLiveData()
    val inputPw        : MutableLiveData<String> = MutableLiveData()
    val inputMakeEmail : MutableLiveData<String> = MutableLiveData()
    val inputName      : MutableLiveData<String> = MutableLiveData()
    val inputMakePW    : MutableLiveData<String> = MutableLiveData()


    fun createUser() {
        compositeDisposable.add(
            repository.createUser(
                email    = inputMakeEmail.value!!,
                name     = inputName.value!!,
                password = inputMakePW.value!!
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Logger.d(it)
                    },
                    {
                        Logger.e(it.message.toString())
                    }
                ))
    }

    fun getUser(){
        compositeDisposable.add(
            repository.getUser(
                email = inputMakeEmail.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Logger.d(it)
                    },
                    {
                        Logger.e(it.message.toString())
                    }
                ))
    }

    fun validateUserInfo(){
//        compositeDisposable.add(
//
//        )
    }

}