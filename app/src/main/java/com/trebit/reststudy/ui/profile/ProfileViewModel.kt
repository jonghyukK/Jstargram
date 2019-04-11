package com.trebit.reststudy.ui.profile

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.Bindable
import android.view.View
import com.orhanobut.logger.Logger
import com.trebit.reststudy.data.model.UserVo
import com.trebit.reststudy.data.remote.ApiService
import com.trebit.reststudy.data.repository.DataRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Jstargram
 * Class: ProfileViewModel
 * Created by kangjonghyuk on 11/04/2019.
 *
 * Description:
 */

class ProfileViewModel @Inject constructor(
    apiService: ApiService
): ViewModel() {

    private val compositeDisposable by lazy { CompositeDisposable() }
    private val repository by lazy { DataRepository(apiService) }

    val myProfileInfo : MutableLiveData<UserVo> = MutableLiveData()
    val inputName     : MutableLiveData<Boolean> = MutableLiveData()

    fun updateUser(email       : String,
                   name        : String,
                   introduce   : String,
                   profile_img : String) {
        compositeDisposable.add(
            repository.updateUser(
                email       = email,
                name        = name,
                introduce   = introduce,
                profile_img = profile_img)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ myProfileInfo.value = it

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

                }, { Logger.e(it.message.toString())})
        )
    }
}