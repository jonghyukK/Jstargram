package com.trebit.reststudy.ui.login.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import com.orhanobut.logger.Logger
import com.trebit.reststudy.RES_FAILED
import com.trebit.reststudy.RES_SUCCESS
import com.trebit.reststudy.data.model.ResponseVo
import com.trebit.reststudy.data.model.ValidateEmailVo
import com.trebit.reststudy.data.remote.ApiService
import com.trebit.reststudy.data.repository.DataRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.xml.datatype.DatatypeConstants.SECONDS

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
    fun requestLogin(email: String, pw: String){
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


    // call Validation Check for Email API.
    fun validateEmail(inputEmail: String,
                      inputPW   : String,
                      inputName : String){
        compositeDisposable.add(
            repository.validateEmail(email = inputEmail)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.resCode) {
                        RES_SUCCESS -> createUser(inputEmail, inputPW, inputName)
                        RES_FAILED  -> signUpResult.value = it
                    }
                    Logger.d("""
                        resCode : ${it.resCode}
                        resMsg  : ${it.resMsg}
                    """.trimIndent())},
                    { Logger.e(it.message.toString()) })
        )
    }


    // call Sign Up User API.
    private fun createUser(inputEmail: String,
                           inputPW   : String,
                           inputName : String) {
        compositeDisposable.add(
            repository.createUser(
                email    = inputEmail,
                password = inputPW,
                name     = inputName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    signUpResult.value = it

                    Logger.d("""
                        resCode : ${it.resCode}
                        resMsg  : ${it.resMsg}
                        """.trimIndent())
                }, { Logger.e(it.message.toString()) }))
    }


//    val test =  { Logger.d("first") }
//    val test2 =  { Logger.d("Second") }
//    val test3 =  { Logger.d("Third") }
//
//    fun chainFunction() {
//        val action : () -> Unit  = test
//        val action2 : () -> Unit = test2
//        val action3 : () -> Unit = test3
//
//        val validateEmail = Observable.just(1)
//            .map { validateEmail("saz300@naver.com") }
//            .doOnComplete(action)
//
//        val validateEmail2 = Observable.just(1)
//            .map { validateEmail("kk@daum,net")}
//            .doOnComplete(action2)
//
//        val source = Observable.concat(validateEmail, validateEmail2)
//            .doOnComplete(action3)
//
//        source.subscribe()
//    }
//
//    val action : (String) -> Unit = {  Logger.e(it)}


}