package com.trebit.reststudy.ui.login.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.trebit.reststudy.RES_FAILED
import com.trebit.reststudy.RES_SUCCESS
import com.trebit.reststudy.data.model.ResponseVo
import com.trebit.reststudy.data.remote.ApiService
import com.trebit.reststudy.data.repository.DataRepository
import io.reactivex.Single
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

    val loginResult  : MutableLiveData<ResponseVo> = MutableLiveData()
    val signUpResult : MutableLiveData<ResponseVo> = MutableLiveData()

    // call Login API.
    fun requestLogin(email: String, pw: String) {
        compositeDisposable.add(
            repository.requestLogin(
                email    = email,
                password = pw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loginResult.value = it

                    Logger.d("""
                        resCode : ${it.resCode}
                        resMsg  : ${it.resMsg}
                        """.trimIndent()
                    )},
                    { Logger.e(it.message.toString()) }
                ))
    }


    // Call Sign Up API.
    fun reqSignUp(email : String,
                  pw    : String,
                  name  : String){

        // 1. Email Validate Check API.
        val validateEmail = repository.validateEmail(email)
            .map {
                when ( it.resCode ) {
                    // success
                    RES_SUCCESS -> Logger.d(it.resMsg)
                    // failed
                    RES_FAILED  -> {
                        signUpResult.postValue(it)
                        throw Exception()
                    }
                }
            }

        // 2. Create User API.
        val createUser = repository.createUser(
            email    = email,
            name     = name,
            password = pw)
            .map {
                when (it.resCode) {
                    // success
                    RES_SUCCESS -> signUpResult.postValue(it)
                    // failed
                    RES_FAILED -> {
                        signUpResult.postValue(it)
                        throw Exception()
                    }
                }
            }

        // 3. Concat (1),(2)
        compositeDisposable.add(
            Single.concat(validateEmail, createUser)
                .lastOrError()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Logger.d("""
                        Email Validate : Pass
                        Create User    : Success
                    """.trimIndent())
                }, { Logger.e("Occur Error Sign Up API Calls.") }))
    }

    override fun onCleared() {
        if(!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        super.onCleared()
    }
}