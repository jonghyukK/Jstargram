package com.trebit.reststudy.ui.main.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import com.trebit.reststudy.*
import com.trebit.reststudy.data.model.ContentItem
import com.trebit.reststudy.data.model.DeleteContentsVo
import com.trebit.reststudy.data.model.UserVo
import com.trebit.reststudy.data.remote.ApiService
import com.trebit.reststudy.data.repository.DataRepository
import com.trebit.reststudy.ui.main.fragment.DataType
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
    val queryUserInfo : MutableLiveData<UserVo> = MutableLiveData()

    val allContents : MutableLiveData<List<ContentItem>> = MutableLiveData()
    val myContents  : MutableLiveData<List<ContentItem>> = MutableLiveData()

    // get User Info by Email.
    fun getUser(email: String){
        compositeDisposable.add(
            repository.getUser(email = email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    myAccountInfo.value = it
                    queryUserInfo.value = it

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

    // get All Contents.
    fun getContents(dataType : String,
                    email    : String? = null) {
        compositeDisposable.add(
            repository.getContents(dataType, email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    when (dataType) {
                        "all" -> {
                            allContents.value = it
                        }
                        "mine" -> {
                            myContents.value = it
                        }
                    }

                    Logger.d("getContents API Result : $it")

                }, { Logger.e(it.message.toString()) }))
    }

    // Delete Contents.
    fun removeContent(contentId: String,
                      email    : String) {
        compositeDisposable.add(
            repository.deleteContent(contentId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    when (it.resCode) {
                        // Success.
                        RES_SUCCESS -> {
                            val copyData = myAccountInfo.value?.copy(contents_cnt = it.contentsCnt)
                            myAccountInfo.postValue(copyData)

                            getContents("all")
                            getContents("mine", email)
                        }
                        // Failed.
                        RES_FAILED -> Logger.e(it.resMsg)
                    }
                    Logger.d(it)
                },{ Logger.e(it.message.toString())}))
    }
}