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

    val isValidEmail   : MutableLiveData<String> = MutableLiveData()
    val signUpResult   : MutableLiveData<String> = MutableLiveData()
    val loginResult    : MutableLiveData<String> = MutableLiveData()


    // call Login API.
    fun requestLogin(){
        compositeDisposable.add(
            repository.requestLogin(
                email    = inputEmail.value!!,
                password = inputPw.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ loginResult.value = it.resCode

                    Logger.d("""
                        resCode : ${it.resCode}
                        resMsg  : ${it.resMsg}
                        """.trimIndent()
                    )},
                    { Logger.e(it.message.toString()) }
                ))
    }

    // call Sign Up User API.
    fun createUser() {
        compositeDisposable.add(
            repository.createUser(
                email    = inputMakeEmail.value!!,
                name     = inputName.value!!,
                password = inputMakePW.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ signUpResult.value = it.resCode

                    Logger.d("""
                        resCode : ${it.resCode}
                        resMsg  : ${it.resMsg}
                        """.trimIndent()
                    )},
                    { Logger.e(it.message.toString()) }
                ))
    }

    // call Validation Check for Email API.
    fun validateEmail() {
        compositeDisposable.add(
            repository.validateEmail(
                email = inputMakeEmail.value!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isValidEmail.value = it.isValidate

                    Logger.d("""
                        resCode   : ${it.resCode}
                        resMsg    : ${it.resMsg}
                        isValidate: ${it.isValidate}
                    """.trimIndent())

                    Logger.d(it)
                },
                    { Logger.e(it.message.toString()) }
                ))
    }

}