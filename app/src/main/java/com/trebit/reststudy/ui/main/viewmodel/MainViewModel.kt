package com.trebit.reststudy.ui.main.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.Build.VERSION_CODES.P
import com.orhanobut.logger.Logger
import com.trebit.reststudy.data.model.UserVo
import com.trebit.reststudy.data.remote.ApiService
import com.trebit.reststudy.data.repository.DataRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Rest_study
 * Class: MainViewModel
 * Created by kangjonghyuk on 28/03/2019.
 *
 * Description:
 */

@Singleton
class MainViewModel @Inject constructor(
    apiService: ApiService
): ViewModel(){

    private val compositeDisposable by lazy { CompositeDisposable() }
    private val repository by lazy { DataRepository(apiService) }

    val myAccountInfo : MutableLiveData<UserVo> = MutableLiveData()

    // call User Info.
    fun getUser(email: String){
        compositeDisposable.add(
            repository.getUser(email = email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ myAccountInfo.value = it

                    Logger.d("""
                        resCode         : ${it.resCode}
                        resMsg          : ${it.resMsg}
                        email           : ${it.email}
                        name            : ${it.name}
                        introduce       : ${it.introduce}
                        profile_img     : ${it.profile_img}
                        contents_cnt    : ${it.contents_cnt}
                        follower_cnt    : ${it.follower_cnt}
                        following_cnt   : ${it.following_cnt}
                    """.trimIndent())

                }, { Logger.e(it.message.toString())}))
    }
}