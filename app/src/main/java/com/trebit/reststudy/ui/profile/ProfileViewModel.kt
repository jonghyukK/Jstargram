package com.trebit.reststudy.ui.profile

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.trebit.reststudy.createRequestBody
import com.trebit.reststudy.data.model.UserVo
import com.trebit.reststudy.data.remote.ApiService
import com.trebit.reststudy.data.repository.DataRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
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

    fun updateUser(name        : String,
                   introduce   : String,
                   email       : String,
                   profile_img : String?) {
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


    fun updateUserWithProfile(name     : String,
                              introduce: String,
                              email    : String,
                              file : MultipartBody.Part){
        compositeDisposable.add(
            repository.updateUserWithProfile(
                name      = createRequestBody(name),
                introduce = createRequestBody(introduce),
                email     = createRequestBody(email),
                file      = file)
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

                }, {
                    Logger.e(it.localizedMessage)
                })
        )
    }
}